package asia.chengfu.pve.service.impl;

import asia.chengfu.pve.core.PVECache;
import asia.chengfu.pve.core.PVERestExecutor;
import asia.chengfu.pve.core.req.PVENodesQemuCloneReq;
import asia.chengfu.pve.core.resp.PVEAccessTicketResp;
import asia.chengfu.pve.core.resp.PVENodesQemuListResp;
import asia.chengfu.pve.core.resp.PVENodesQemuStatusCurrentResp;
import asia.chengfu.pve.service.PVEService;
import asia.chengfu.pve.service.PVEServiceProxy;
import asia.chengfu.pve.service.req.VMCloneReq;
import cn.hutool.aop.ProxyUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class PVEServiceImpl implements PVEService {

    private static final int DEFAULT_MIN_VMID = 100;

    @Override
    public void deleteVm(String node, String... vmids) {
        checkAndRefreshToken();

        for (String vmid : vmids) {
            PVERestExecutor.deleteNodesQemu(node, vmid);
        }
    }

    @Override
    public void stopVm(String node, String... vmids) {
        checkAndRefreshToken();

        for (String vmid : vmids) {
            PVERestExecutor.postNodesQemuStatusStop(node, vmid);
        }
    }

    @Override
    public List<String> cloneVm(String node, String vmid, VMCloneReq req) {
        checkAndRefreshToken();

        // 克隆的数量
        int cloneNumber = req.getCloneNumber();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<CompletableFuture<Void>> futures = new ArrayList<>(cloneNumber);
        List<String> vmids = new ArrayList<>(cloneNumber);
        try {

            for (int i = 0; i < cloneNumber; i++) {
                String vmNameSuffix = StrUtil.fill(StrUtil.toString(i + 1), '0', 2, true);
                String vmName = req.getCloneVmNamePrefix() + "-" + vmNameSuffix;

                String id = StrUtil.toString(getNewVmid(node));
                vmids.add(id);

                StaticLog.debug("VMID --- {} {}", id, Thread.currentThread().getName());

                cloneVmStage1Clone(node, vmid, vmName, id);

                CompletableFuture<Void> completableFuture =
                        CompletableFuture.runAsync(() -> {
                                    cloneVmStage2Listener(node, id);
                                }, executorService)
                                .thenAcceptAsync(v -> {
                                    cloneVmStage3Start(node, id);
                                }, executorService);

                futures.add(completableFuture);
            }


            for (CompletableFuture<Void> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }

        return vmids;
    }

    private static void cloneVmStage3Start(String node, String vmid) {
        PVERestExecutor.nodesQemuStatusStart(node, vmid);
    }

    private static void cloneVmStage2Listener(String node, String vmid) {
        int maxAttempt = 30;
        int index = 0;
        while (index < maxAttempt) {
            ThreadUtil.sleep(30, TimeUnit.SECONDS);
            PVENodesQemuStatusCurrentResp currentResp = PVERestExecutor.nodesQemuStatusCurrent(node, vmid);
            boolean completeFlag = StrUtil.equals(currentResp.getLock(), "clone");
            if (!completeFlag) {
                return;
            }
            index++;
        }
    }

    private static void cloneVmStage1Clone(String node, String vmid, String vmName, String id) {
        PVENodesQemuCloneReq pveNodesQemuCloneReq = new PVENodesQemuCloneReq();
        pveNodesQemuCloneReq.setNode(node);
        pveNodesQemuCloneReq.setName(vmName);
        pveNodesQemuCloneReq.setFull(true);  //完整克隆
        pveNodesQemuCloneReq.setNewid(id);
        PVERestExecutor.nodesQemuClone(node, vmid, pveNodesQemuCloneReq);
    }

    /**
     * 获取新的虚拟机ID
     *
     * @param node 节点名称
     * @return 虚拟机ID
     */
    private int getNewVmid(String node) {
        var vmInfos = PVERestExecutor
                .listNodesQemu(node)
                .toArray(new PVENodesQemuListResp[0]);

        Arrays.sort(vmInfos);

        int curVmid = DEFAULT_MIN_VMID;
        for (PVENodesQemuListResp vmInfo : vmInfos) {
            if (curVmid == vmInfo.getVmid()) {
                curVmid += 1;
            } else {
                return curVmid;
            }
        }

        return curVmid;
    }

    private void checkAndRefreshToken() {
        if (PVECache.checkRefreshFlag()) {
            PVEAccessTicketResp PVEAccessTicketResp = PVERestExecutor.accessTicket();
            PVECache.setToken(PVEAccessTicketResp.getCSRFPreventionToken());
            PVECache.setCookie(PVEAccessTicketResp.getTicket());
            PVECache.closeRefreshFlag();
        }
    }

    public static void main(String[] args) {
        PVECache.setPveHost("10.0.0.85");
        PVECache.setPveLoginUser("root@pam");
        PVECache.setPveLoginPass("Vst123");

//        System.out.println(PVEService.getNewVmid("node1"));

//        VMCloneReq vmCloneReq = new VMCloneReq();
//        vmCloneReq.setCloneNumber(5);
//        vmCloneReq.setCloneVmNamePrefix("DEMO");
//        PVEService.cloneVm("node1", "111", vmCloneReq);

//        PVEService.deleteVm("node1", "112", "114", "123", "124", "125", "126", "127");

//        ProxyUtil.newProxyInstance()

//        PVEService.INSTANCE.deleteVm("node1", "112", "114", "123", "124", "125", "126", "127");

        PVEService pveService = ProxyUtil.newProxyInstance(new PVEServiceProxy(), PVEService.class);

//        VMCloneReq vmCloneReq = new VMCloneReq();
//        vmCloneReq.setCloneVmNamePrefix("DEMO");
//        vmCloneReq.setCloneNumber(3);
//        List<String> vmids = pveService.cloneVm("node1", "104", vmCloneReq);
//        System.out.println(vmids);

//        for (Integer vmid : vmids) {
//            ThreadUtil.execAsync(() -> {
//                System.out.println(PVERestExecutor.nodesQemuStatusCurrent("node1", StrUtil.toString(vmid)));
//            });
//        }

//        pveService.stopVm("node1", "109", "112", "114");
//        cloneVmStage2Listener();
        pveService.deleteVm("node1", "109", "112", "114");
    }




    //91vst.com
    // 用户名，姓，名，密码，计算机名，域名
}

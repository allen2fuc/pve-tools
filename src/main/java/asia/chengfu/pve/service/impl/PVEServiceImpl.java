package asia.chengfu.pve.service.impl;

import asia.chengfu.pve.core.PVECache;
import asia.chengfu.pve.core.PVERestExecutor;
import asia.chengfu.pve.core.PveRestConfig;
import asia.chengfu.pve.core.exception.HttpStatusException;
import asia.chengfu.pve.core.req.AccessAclReq;
import asia.chengfu.pve.core.req.PVENodesQemuCloneReq;
import asia.chengfu.pve.core.req.PVENodesQemuConfigReq;
import asia.chengfu.pve.core.resp.*;
import asia.chengfu.pve.service.PVEService;
import asia.chengfu.pve.service.req.AddAccessAclReq;
import asia.chengfu.pve.service.req.VMCloneReq;
import asia.chengfu.pve.service.resp.VMListInfo;
import asia.chengfu.pve.service.tool.ScheduleFunctionTask;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PVEServiceImpl implements PVEService {

    private static final int DEFAULT_MIN_VMID = 100;

    private PVERestExecutor executor;

    public PVEServiceImpl(String host, String username, String password) {
        this.executor = new PVERestExecutor(PveRestConfig.builder()
                .host(host)
                .username(username)
                .password(password)
                .build());
    }

    /**
     * 删除虚拟机
     *
     * @param node  节点名称
     * @param vmids 需要删除的虚拟机ID列表
     */
    @Override
    public void deleteVm(String node, String... vmids) {
        checkAndRefreshToken();

        for (String vmid : vmids) {
            executor.deleteNodesQemu(node, vmid);
        }
    }

    @Override
    public void addAccessAcl(AddAccessAclReq... reqs) {
        checkAndRefreshToken();

        for (AddAccessAclReq req : reqs) {
            AccessAclReq accessAclReq = new AccessAclReq();
            BeanUtil.copyProperties(req, accessAclReq);
            executor.accessAcl(accessAclReq);
        }
    }

    /**
     * 获取IP重制到tag中
     *
     * @param node     节点名称
     * @param ipPrefix 匹配IP前缀
     * @param vmids    需要检索的VMID集合
     */
    @Override
    public void postPlaceIPToTag(String node, String ipPrefix, String... vmids) {
        checkAndRefreshToken();

        for (String vmid : vmids) {
            IPv4Info ipv4Info = findIpv4Info(node, vmid);
            if (ObjUtil.isNotNull(ipv4Info)){
                PVENodesQemuConfigReq qemuConfigReq = new PVENodesQemuConfigReq();
                qemuConfigReq.setTags(ipv4Info.getIp());
                executor.postNodesQemuConfig(node, vmid, qemuConfigReq);
            }
        }
    }

    /**
     * 追加Tag
     *
     * @param node 节点名称
     * @param vmid 虚拟机ID
     * @param tag  标签信息
     */
    @Override
    public void appendTag(String node, String vmid, String tag) {
        checkAndRefreshToken();

        PVENodesQemuConfigResp nodesQemuConfig = executor.getNodesQemuConfig(node, vmid);
        String tags = nodesQemuConfig.getTags();

        String newTags;
        if (StrUtil.isNotBlank(tag)) {
            newTags = StrUtil.format("{};{}", tags, tag);
        } else {
            newTags = tag;
        }

        PVENodesQemuConfigReq qemuConfigReq = new PVENodesQemuConfigReq();
        qemuConfigReq.setTags(newTags);

        executor.postNodesQemuConfig(node, vmid, qemuConfigReq);
    }

    /**
     * 停止虚拟机
     *
     * @param node  节点名称
     * @param vmids 虚拟机ID列表
     */
    @Override
    public void stopVm(String node, String... vmids) {
        checkAndRefreshToken();

        for (String vmid : vmids) {
            executor.nodesQemuStatusStop(node, vmid);
        }
    }

    @Override
    public void rebootVm(String node, String... vmids) {
        checkAndRefreshToken();

        for (String vmid : vmids) {
            executor.nodesQemuStatusReboot(node, vmid);
        }
    }

    /**
     * 批量克隆虚拟机
     *
     * @param node 节点名称
     * @param vmid 克隆的虚拟机ID
     * @param req  请求信息
     * @return 新创建的虚拟机ID
     */
    @Override
    public List<String> cloneVm(String node, String vmid, VMCloneReq req) {
        checkAndRefreshToken();

        // 克隆的数量
        int cloneNumber = req.getCloneNumber();

        ExecutorService executorService = Executors.newFixedThreadPool(cloneNumber);
        List<CompletableFuture<String>> futures = new ArrayList<>(cloneNumber);
        List<String> vmids = new ArrayList<>(cloneNumber);
        try {

            for (int i = 0; i < cloneNumber; i++) {
                int startIndex = req.getStartIndex();
                String vmNameSuffix = StrUtil.fill(StrUtil.toString(startIndex + i), '0', 2, true);
                String vmName = req.getCloneVmNamePrefix() + "-" + vmNameSuffix;

                String id = StrUtil.toString(getNewVmid(node));
                vmids.add(id);

                StaticLog.debug("VMID --- {} {}", id, Thread.currentThread().getName());

                cloneVmStage1Clone(node, vmid, vmName, id);

                CompletableFuture<String> completableFuture =
                        CompletableFuture.supplyAsync(() -> cloneVmStage2ListenerCloneCompleted(node, id), executorService)
                                .thenApplyAsync((resp) -> cloneVmStage3Start(node, id), executorService)
                                .thenApplyAsync((resp) -> cloneVmStage4GetIp(node, id), executorService)
                                .thenApplyAsync((resp) -> cloneVmStage5PutTag(node, id, resp), executorService);

                futures.add(completableFuture);
            }


            for (CompletableFuture<String> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }

        return vmids;
    }

    @Override
    public void addVmDomain(String node, String vmid, String domainName, String adminPassword) {
        String domainPrefix = CollUtil.getFirst(StrUtil.split(domainName, "."));
        String inputData = StrUtil.format("""
                $secpasswd = ConvertTo-SecureString '{}' -AsPlainText -Force; 
                $mycreds = New-Object System.Management.Automation.PSCredential ('{}\\Administrator', $secpasswd); 
                Add-Computer -DomainName '{}' -Credential $mycreds -Restart
                """, adminPassword, domainPrefix, domainName);
        PVENodesQemuAgentExecResp resp = executor.nodesQemuAgentExecPowershell(node, vmid, inputData);

        StaticLog.debug("Resp: {}", resp);
    }

    /**
     * 虚拟机列表
     *
     * @param node         节点名称
     * @param vmNamePrefix 虚拟机名称前缀
     * @return 虚拟机列表信息
     */
    @Override
    public List<VMListInfo> listVm(String node, String vmNamePrefix) {
        checkAndRefreshToken();

        List<PVENodesQemuListResp> qemuListResps = executor.listNodesQemu(node);

        ArrayList<VMListInfo> results = new ArrayList<>();
        for (PVENodesQemuListResp qemuListResp : qemuListResps) {
            if (StrUtil.startWithAnyIgnoreCase(qemuListResp.getName(), vmNamePrefix)) {

                VMListInfo info = new VMListInfo();
                info.setVmid(qemuListResp.getVmid());
                info.setName(qemuListResp.getName());
                info.setStatus(qemuListResp.getStatus());
                info.setTags(qemuListResp.getTags());

                IPv4Info ipv4Info = findIpv4Info(node, StrUtil.toString(qemuListResp.getVmid()));
                if (ObjectUtil.isNotNull(ipv4Info)){
                    info.setIp(ipv4Info.getIp());
                    info.setMac(ipv4Info.getMac());
                }

                results.add(info);
            }
        }

        ListUtil.sort(results, new VMListInfo());

        return results;
    }

    /**
     * 生成RDP文件
     *
     * @param username 用户名称
     * @param ip       IP定制
     * @param rdpFile  文件目的地
     */
    @Override
    public void generateRDPFile(String username, String ip, String rdpFile) {
        Map<Object, Object> params = MapUtil.builder().put("ip", ip).put("username", username).build();
        String rdpContent = StrUtil.format(
                """
                        screen mode id:i:2
                        use multimon:i:0
                        desktopwidth:i:1920
                        desktopheight:i:1080
                        session bpp:i:32
                        winposstr:s:0,3,0,0,800,600
                        compression:i:1
                        keyboardhook:i:2
                        audiocapturemode:i:0
                        videoplaybackmode:i:1
                        connection type:i:7
                        networkautodetect:i:1
                        bandwidthautodetect:i:1
                        displayconnectionbar:i:1
                        username:s:{username}
                        enableworkspacereconnect:i:0
                        disable wallpaper:i:0
                        allow font smoothing:i:0
                        allow desktop composition:i:0
                        disable full window drag:i:1
                        disable menu anims:i:1
                        disable themes:i:0
                        disable cursor setting:i:0
                        bitmapcachepersistenable:i:1
                        full address:s:{ip}
                        audiomode:i:0
                        redirectprinters:i:1
                        redirectcomports:i:0
                        redirectsmartcards:i:1
                        redirectclipboard:i:1
                        redirectposdevices:i:0
                        autoreconnection enabled:i:1
                        authentication level:i:2
                        prompt for credentials:i:0
                        negotiate security layer:i:1
                        remoteapplicationmode:i:0
                        alternate shell:s:
                        shell working directory:s:
                        gatewayhostname:s:
                        gatewayusagemethod:i:4
                        gatewaycredentialssource:i:4
                        gatewayprofileusagemethod:i:0
                        promptcredentialonce:i:0
                        gatewaybrokeringtype:i:0
                        use redirection server name:i:0
                        rdgiskdcproxy:i:0
                        kdcproxyname:s:
                        """, params);

        FileUtil.writeUtf8String(rdpContent, rdpFile);
    }

    private String cloneVmStage5PutTag(String node, String vmid, String tags) {
        PVENodesQemuConfigReq req = new PVENodesQemuConfigReq();
        req.setTags(tags);
        String resp = executor.postNodesQemuConfig(node, vmid, req);

        StaticLog.debug("克隆第五阶段 - 放置tag VMID：{},响应：{}", vmid, resp);

        return resp;
    }

    private String cloneVmStage4GetIp(String node, String vmid) {
        Supplier<IPv4Info> supplier = () -> findIpv4Info(node, vmid);
        Predicate<IPv4Info> predicate = (resp) -> true;
        ScheduleFunctionTask<IPv4Info> scheduleFunctionTask = new ScheduleFunctionTask<>(supplier, predicate);
        scheduleFunctionTask.scheduleJoin(10, 5, TimeUnit.SECONDS);
        String resp = scheduleFunctionTask.getOutput().getIp();

        StaticLog.debug("克隆第四阶段 - 获取IP VMID：{},IP地址：{}", vmid, resp);

        return resp;
    }

    private String cloneVmStage3Start(String node, String vmid) {
        String resp = executor.nodesQemuStatusStart(node, vmid);

        StaticLog.debug("克隆第三阶段 - 启动成功的ID：{},响应：{}", vmid, resp);

        return resp;

    }

    private String cloneVmStage2ListenerCloneCompleted(String node, String vmid) {

        Supplier<PVENodesQemuStatusCurrentResp> supplier = () -> executor.nodesQemuStatusCurrent(node, vmid);
        Predicate<PVENodesQemuStatusCurrentResp> predicate = (resp) -> !StrUtil.equals(resp.getLock(), "clone");
        ScheduleFunctionTask<PVENodesQemuStatusCurrentResp> scheduleFunctionTask = new ScheduleFunctionTask<>(supplier, predicate);
        scheduleFunctionTask.scheduleJoin(10, 5, TimeUnit.SECONDS);

        String newVmid = StrUtil.toString(scheduleFunctionTask.getOutput().getVmid());

        StaticLog.debug("克隆第二阶段 - 监听克隆状态原ID：{},新ID：{}", vmid, newVmid);

        return newVmid;
    }

    private void cloneVmStage1Clone(String node, String vmid, String vmName, String id) {
        PVENodesQemuCloneReq pveNodesQemuCloneReq = new PVENodesQemuCloneReq();
        pveNodesQemuCloneReq.setNode(node);
        pveNodesQemuCloneReq.setName(vmName);
        pveNodesQemuCloneReq.setFull(true);  //完整克隆
        pveNodesQemuCloneReq.setNewid(id);

        String resp = executor.nodesQemuClone(node, vmid, pveNodesQemuCloneReq);

        StaticLog.debug("克隆第一阶段 - 克隆原ID：{},响应：{}", vmid, resp);

    }

    /**
     * 获取新的虚拟机ID
     *
     * @param node 节点名称
     * @return 虚拟机ID
     */
    private int getNewVmid(String node) {
        var vmInfos = executor.listNodesQemu(node)
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
            PVEAccessTicketResp PVEAccessTicketResp = executor.accessTicket();
            PVECache.setToken(PVEAccessTicketResp.getCSRFPreventionToken());
            PVECache.setCookie(PVEAccessTicketResp.getTicket());
            PVECache.closeRefreshFlag();
        }
    }

    private IPv4Info findIpv4Info(String node, String vmid) {
        try {
            PVENodesQemuAgentNetworkResp agentNetwork = executor.nodesQemuAgentNetwork(node, vmid);

            for (PVENodesQemuAgentNetworkResp.QemuAgentNetworkGetInterfaceResult result : agentNetwork.getResult()) {
                boolean isEthernet = StrUtil.equals(result.getName(), "以太网");
                if (!isEthernet) {
                    continue;
                }
                for (PVENodesQemuAgentNetworkResp.QemuAgentNetworkGetInterfaceIpAddress ipAddress : result.getIpAddresses()) {
                    boolean isIpv4 = StrUtil.equals(ipAddress.getIpAddressType(), "ipv4");
                    if (!isIpv4) {
                        continue;
                    }

                    String hardwareAddress = result.getHardwareAddress();
                    String ipAddressStr = ipAddress.getIpAddress();

                    return new IPv4Info(ipAddressStr, hardwareAddress);
                }
            }
        } catch (HttpStatusException _) {

        }

        return null;
    }

    private static class IPv4Info {
        private String ip;
        private String mac;

        public IPv4Info(String ip, String mac) {
            this.ip = ip;
            this.mac = mac;
        }

        public String getIp() {
            return ip;
        }

        public String getMac() {
            return mac;
        }
    }

}

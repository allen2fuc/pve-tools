package asia.chengfu.pve.core;

import asia.chengfu.pve.core.req.PVEAccessTicketReq;
import asia.chengfu.pve.core.req.PVENodesQemuAgentPasswordReq;
import asia.chengfu.pve.core.req.PVENodesQemuCloneReq;
import asia.chengfu.pve.core.req.PVENodesQemuConfigReq;
import asia.chengfu.pve.core.resp.*;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.Method;

import java.util.List;

public class PVERestExecutor {

    public static PVEAccessTicketResp accessTicket() {
        var req = new PVEAccessTicketReq();
        req.setUsername(PVECache.getPveLoginUser());
        req.setPassword(PVECache.getPveLoginPass());

        var param = PVEHttpParam.<PVEAccessTicketReq, PVEAccessTicketResp>builder()
                .restUri(PVERestUri.ACCESS_TICKET)
                .method(Method.POST)
                .respClazz(new TypeReference<>() {})
                .req(req)
                .build();

        return PVEHttpExecutor.execute(param);
    }

    public static List<PVENodesQemuListResp> listNodesQemu(String node){
        PVECache.setCurrentNode(node);

        var param = PVEHttpParam.<Void, List<PVENodesQemuListResp>>builder()
                .restUri(PVERestUri.NODES_QEMU_LIST)
                .method(Method.GET)
                .respClazz(new TypeReference<>() {})
                .build();
        return PVEHttpExecutor.execute(param);
    }

    public static String deleteNodesQemu(String node, String vmid){
        PVECache.setCurrentNode(node);

        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_DELETE)
                .method(Method.DELETE)
                .respClazz(new TypeReference<>() {})
                .build();
        return PVEHttpExecutor.execute(param);
    }

//    public static String putNodesQemuConfig(String node, String vmid, NodesQemuConfigReq req){
//        PVECache.setCurrentNode(node);
//        PVECache.setCurrentVmid(vmid);
//
//
//        var param = PVEHttpParam.<NodesQemuConfigReq, String>builder()
//                .restUri(PVERestUri.NODES_QEMU_CONFIG)
//                .method(Method.PUT)
//                .req(req)
//                .respClazz(new TypeReference<>() {})
//                .build();
//
//        return PVEHttpExecutor.execute(param);
//    }

    public static String postNodesQemuConfig(String node, String vmid, PVENodesQemuConfigReq req){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);


        var param = PVEHttpParam.<PVENodesQemuConfigReq, String>builder()
                .restUri(PVERestUri.NODES_QEMU_CONFIG)
                .method(Method.POST)
                .req(req)
                .respClazz(new TypeReference<>() {})
                .build();

        return PVEHttpExecutor.execute(param);
    }

    public static PVENodesQemuConfigResp getNodesQemuConfig(String node, String vmid){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<Void, PVENodesQemuConfigResp>builder()
                .restUri(PVERestUri.NODES_QEMU_CONFIG)
                .method(Method.GET)
                .respClazz(new TypeReference<>() {})
                .build();

        return PVEHttpExecutor.execute(param);
    }

    public static String nodesQemuAgentPassword(String node, String vmid, PVENodesQemuAgentPasswordReq req){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<PVENodesQemuAgentPasswordReq, String>builder()
                .restUri(PVERestUri.NODES_QEMU_AGENT_PASSWORD)
                .method(Method.POST)
                .req(req)
                .respClazz(new TypeReference<>() {})
                .build();

        return PVEHttpExecutor.execute(param);

    }

    public static PVENodesQemuAgentNetworkResp getNodesQemuAgentNetwork(String node, String vmid){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<Void, PVENodesQemuAgentNetworkResp>builder()
                .restUri(PVERestUri.NODES_QEMU_AGENT_NETWORK)
                .method(Method.GET)
                .respClazz(new TypeReference<>() {
                })
                .build();
        return PVEHttpExecutor.execute(param);
    }

    public static String postNodesQemuStatusStop(String node, String vmid){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_STATUS_STOP)
                .method(Method.POST)
                .respClazz(new TypeReference<>() {})
                .build();

        //UPID:node1:000137A1:0013B1AF:670A041C:qmstop:109:root@pam:
        return PVEHttpExecutor.execute(param);
    }

    public static String nodesQemuAgentShutdown(String node, String vmid){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_AGENT_SHUTDOWN)
                .method(Method.POST)
                .respClazz(new TypeReference<>() {})
                .build();

        return PVEHttpExecutor.execute(param);
    }

    public static String nodesQemuStatusStart(String node, String vmid){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_STATUS_START)
                .method(Method.POST)
                .respClazz(new TypeReference<>() {})
                .build();

        return PVEHttpExecutor.execute(param);
    }

    public static String nodesQemuClone(String node, String vmid, PVENodesQemuCloneReq req){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<PVENodesQemuCloneReq, String>builder()
                .restUri(PVERestUri.NODES_QEMU_CLONE)
                .method(Method.POST)
                .req(req)
                .respClazz(new TypeReference<>() {})
                .build();

        return PVEHttpExecutor.execute(param);
    }

    public static PVENodesQemuStatusCurrentResp nodesQemuStatusCurrent(String node, String vmid){
        PVECache.setCurrentNode(node);
        PVECache.setCurrentVmid(vmid);

        var param = PVEHttpParam.<Void, PVENodesQemuStatusCurrentResp>builder()
                .restUri(PVERestUri.NODES_QEMU_STATUS_CURRENT)
                .method(Method.GET)
                .respClazz(new TypeReference<>() {})
                .build();

        return PVEHttpExecutor.execute(param);
    }

    public static void main(String[] args) {
        PVECache.setPveHost("10.0.0.85");
        PVECache.setPveLoginUser("root@pam");
        PVECache.setPveLoginPass("Vst123");

        PVEAccessTicketResp PVEAccessTicketResp = PVERestExecutor.accessTicket();
        System.out.println(PVEAccessTicketResp);

        PVECache.setToken(PVEAccessTicketResp.getCSRFPreventionToken());
        PVECache.setCookie(PVEAccessTicketResp.getTicket());

//        List<PVENodesQemuListResp> nodesQemuListResps = PVERestExecutor.listNodesQemu("node1");
//        PrinterTool.tablePrint(nodesQemuListResps);


        System.out.println(PVERestExecutor.nodesQemuStatusCurrent("node1", "109"));

//        PVENodesQemuAgentPasswordReq pveNodesQemuAgentPasswordReq = new PVENodesQemuAgentPasswordReq();
//        pveNodesQemuAgentPasswordReq.setUsername("user");
//        pveNodesQemuAgentPasswordReq.setPassword("123456");
//        System.out.println(nodesQemuAgentPassword("node1", "100", pveNodesQemuAgentPasswordReq));

//
//        NodesQemuAgentNetworkResp nodesQemuAgentNetworkResp = PVERestExecutor.nodesQemuAgentNetwork("node1", "117");
//        System.out.println(nodesQemuAgentNetworkResp);

//        nodesQemuStatusStop("node1", "109");

//        postNodesQemuAgentShutdown("node1", "109");

//        String[] vmids = {"112", "114", ""};
//        PVERestExecutor.deleteNodesQemu("node1", "109");

//        NodesQemuConfigReq nodesQemuConfigReq = new NodesQemuConfigReq();
//        nodesQemuConfigReq.setTags("aaa;bbb");
//        PVERestExecutor.postNodesQemuConfig("node1", "116", nodesQemuConfigReq);

//        System.out.println(PVERestExecutor.getNodesQemuConfig("node1", "117"));

//        NodesQemuAgentNetworkResp nodesQemuAgentNetwork = getNodesQemuAgentNetwork("node1", "117");
//        System.out.println(nodesQemuAgentNetwork);

//        PVENodesQemuCloneReq nodesQemuCloneReq = new PVENodesQemuCloneReq();

//        nodesQemuClone("node1", "111", nodesQemuCloneReq);
    }
}

package asia.chengfu.pve.core;

import asia.chengfu.pve.core.req.*;
import asia.chengfu.pve.core.resp.*;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.Method;

import java.util.List;

public class PVERestExecutor {
    private PveRestConfig config;

    public PVERestExecutor(PveRestConfig config){
        this.config = config;
    }

    /**
     * 获取Token
     * pvesh create /access/ticket --username root@pam --password Vst123
     * @return Token
     */
    public PVEAccessTicketResp accessTicket() {
        var req = new PVEAccessTicketReq();
        req.setUsername(config.getUsername());
        req.setPassword(config.getPassword());

        var param = PVEHttpParam.<PVEAccessTicketReq, PVEAccessTicketResp>builder()
                .restUri(PVERestUri.ACCESS_TICKET)
                .method(Method.POST)
                .respClazz(new TypeReference<>() {})
                .req(req)
                .build();

        return execute(param);
    }

    public void accessAcl(AccessAclReq req){
        var param = PVEHttpParam.<AccessAclReq, Void>builder()
                .restUri(PVERestUri.ACCESS_ACL)
                .method(Method.PUT)
                .respClazz(new TypeReference<>() {})
                .req(req)
                .build();

        execute(param);
    }

    public List<PVENodesQemuListResp> listNodesQemu(String node){
        var param = PVEHttpParam.<Void, List<PVENodesQemuListResp>>builder()
                .restUri(PVERestUri.NODES_QEMU_LIST)
                .method(Method.GET)
                .respClazz(new TypeReference<>() {})
                .build();
        return execute(node, param);
    }

    public String deleteNodesQemu(String node, String vmid){
        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_DELETE)
                .method(Method.DELETE)
                .respClazz(new TypeReference<>() {})
                .build();
        return execute(node, vmid, param);
    }

    public String postNodesQemuConfig(String node, String vmid, PVENodesQemuConfigReq req){
        var param = PVEHttpParam.<PVENodesQemuConfigReq, String>builder()
                .restUri(PVERestUri.NODES_QEMU_CONFIG)
                .method(Method.POST)
                .req(req)
                .respClazz(new TypeReference<>() {})
                .build();

        return execute(node, vmid, param);
    }

    public PVENodesQemuConfigResp getNodesQemuConfig(String node, String vmid){
        var param = PVEHttpParam.<Void, PVENodesQemuConfigResp>builder()
                .restUri(PVERestUri.NODES_QEMU_CONFIG)
                .method(Method.GET)
                .respClazz(new TypeReference<>() {})
                .build();
        return execute(node, vmid, param);
    }

    public String nodesQemuAgentPassword(String node, String vmid, PVENodesQemuAgentPasswordReq req){
        var param = PVEHttpParam.<PVENodesQemuAgentPasswordReq, String>builder()
                .restUri(PVERestUri.NODES_QEMU_AGENT_PASSWORD)
                .method(Method.POST)
                .req(req)
                .respClazz(new TypeReference<>() {})
                .build();
        return execute(node, vmid, param);
    }

    public PVENodesQemuAgentExecResp nodesQemuAgentExec(String node, String vmid, PVENodesQemuAgentExecReq req){
        var param = PVEHttpParam.<PVENodesQemuAgentExecReq, PVENodesQemuAgentExecResp>builder()
                .restUri(PVERestUri.NODES_QEMU_AGENT_EXEC)
                .method(Method.POST)
                .req(req)
                .respClazz(new TypeReference<>() {})
                .build();
        return execute(node, vmid, param);
    }

    public PVENodesQemuAgentExecResp nodesQemuAgentExecPowershell(String node, String vmid, String inputData){
        PVENodesQemuAgentExecReq pveNodesQemuAgentExecReq = new PVENodesQemuAgentExecReq();
        pveNodesQemuAgentExecReq.setCommand("powershell.exe");
        pveNodesQemuAgentExecReq.setInputData(inputData);
        return nodesQemuAgentExec(node, vmid, pveNodesQemuAgentExecReq);
    }


    public PVENodesQemuAgentNetworkResp nodesQemuAgentNetwork(String node, String vmid){
        var param = PVEHttpParam.<Void, PVENodesQemuAgentNetworkResp>builder()
                .restUri(PVERestUri.NODES_QEMU_AGENT_NETWORK)
                .method(Method.GET)
                .respClazz(new TypeReference<>() {
                })
                .build();
        return execute(node, vmid, param);
    }

    public String nodesQemuStatusStop(String node, String vmid){
        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_STATUS_STOP)
                .method(Method.POST)
                .respClazz(new TypeReference<>() {})
                .build();

        return execute(node, vmid, param);
    }

    public String nodesQemuAgentShutdown(String node, String vmid){
        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_AGENT_SHUTDOWN)
                .method(Method.POST)
                .respClazz(new TypeReference<>() {})
                .build();

        return execute(node, vmid, param);
    }

    public String nodesQemuStatusStart(String node, String vmid){
        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_STATUS_START)
                .method(Method.POST)
                .respClazz(new TypeReference<>() {})
                .build();

        return execute(node, vmid, param);
    }

    public String nodesQemuStatusReboot(String node, String vmid){
        var param = PVEHttpParam.<Void, String>builder()
                .restUri(PVERestUri.NODES_QEMU_STATUS_REBOOT)
                .method(Method.POST)
                .respClazz(new TypeReference<String>() {})
                .build();

        return execute(node, vmid, param);
    }

    public String nodesQemuClone(String node, String vmid, PVENodesQemuCloneReq req){
        var param = PVEHttpParam.<PVENodesQemuCloneReq, String>builder()
                .restUri(PVERestUri.NODES_QEMU_CLONE)
                .method(Method.POST)
                .req(req)
                .respClazz(new TypeReference<>() {})
                .build();

        return execute(node, vmid, param);
    }

    public PVENodesQemuStatusCurrentResp nodesQemuStatusCurrent(String node, String vmid){
        var param = PVEHttpParam.<Void, PVENodesQemuStatusCurrentResp>builder()
                .restUri(PVERestUri.NODES_QEMU_STATUS_CURRENT)
                .method(Method.GET)
                .respClazz(new TypeReference<>() {})
                .build();

        return execute(node, vmid, param);
    }

    private <T> T execute(PVEHttpParam<T> param) {
        return execute(null, null, param);
    }

    private <T> T execute(String node, PVEHttpParam<T> param) {
        return execute(node, null, param);
    }

    private <T> T execute(String node, String vmid, PVEHttpParam<T> param){
        return PVEHttpExecutor.execute(node, vmid, param, config);
    }

    public static void main(String[] args) {
        PVERestExecutor executor = new PVERestExecutor(PveRestConfig.builder().host("10.0.0.85")
                .username("root@pam")
                .password("Vst123")
                .build());

        PVEAccessTicketResp PVEAccessTicketResp = executor.accessTicket();
        System.out.println(PVEAccessTicketResp);
//
        PVECache.setToken(PVEAccessTicketResp.getCSRFPreventionToken());
        PVECache.setCookie(PVEAccessTicketResp.getTicket());

//        List<PVENodesQemuListResp> nodesQemuListResps = PVERestExecutor.listNodesQemu("node1");
//        PrinterTool.tablePrint(nodesQemuListResps);


        System.out.println(executor.nodesQemuStatusCurrent("node1", "109"));

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

package asia.chengfu.pve.core.resp;

public class PVENodesQemuAgentExecResp {
    private int pid;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "PVENodesQemuAgentExecResp{" +
                "pid=" + pid +
                '}';
    }
}

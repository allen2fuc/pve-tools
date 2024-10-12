package asia.chengfu.pve.core.req;

public class PVENodesQemuCloneReq {
    private String newid;
    private String node;

    private String name;

    /**
     * 克隆方式
     * true:完整克隆，false:链接克隆
     */
    private boolean full;

    public String getNewid() {
        return newid;
    }

    public void setNewid(String newid) {
        this.newid = newid;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    @Override
    public String toString() {
        return "NodesQemuCloneReq{" +
                "newid='" + newid + '\'' +
                ", node='" + node + '\'' +
                ", name='" + name + '\'' +
                ", full=" + full +
                '}';
    }
}

package asia.chengfu.pve.core.resp;

public class PVEClusterStatusResp {

    private String id;
    private int local;
    private int nodeid;
    private String level;
    private String name;
    private String type;
    private String ip;
    private int online;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        this.local = local;
    }

    public int getNodeid() {
        return nodeid;
    }

    public void setNodeid(int nodeid) {
        this.nodeid = nodeid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "ClusterStatusResp{" +
                "id='" + id + '\'' +
                ", local=" + local +
                ", nodeid=" + nodeid +
                ", level='" + level + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", online=" + online +
                '}';
    }
}

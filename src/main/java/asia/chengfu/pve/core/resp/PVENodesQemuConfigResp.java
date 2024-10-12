package asia.chengfu.pve.core.resp;

public class PVENodesQemuConfigResp {
    private String name; //": "doracloud4.0-20240802",
    private String boot; //": "order=scsi0;ide2;net0",
    private int cores; //": 2,
    private int sockets; //": 1,
    private String memory; //": "2048",
    private String ide2; //": "none,media=cdrom",
    private String agent; //": "1",
    private int numa; //": 0,
    private int balloon; //": 0,
    private String scsihw; //": "virtio-scsi-single",
    private String ostype; //": "l26",
    private String meta; //": "creation-qemu=7.2.0,ctime=1714790250",
    private String scsi0; //": "user-data:vm-117-disk-0,aio=threads,iothread=1,size=16G,ssd=1",
    private String cpu; //": "host",
    private String vmgenid; //": "7f418c81-8d3c-406b-9ea8-c29f316bc8df",
    private String tags; //": "10.0.0.95",
    private String digest; //": "8e1c19084d26057b6f47d6b122556e9dcd5ab5a9",
    private String net0; //": "virtio=06:EC:68:08:E4:DF,bridge=vmbr0,firewall=1",
    private String smbios1; //": "uuid=00e4eae3-8c41-4c3c-9b82-15cb10ec65f6",
    private int onboot; //": 1

    public String getBoot() {
        return boot;
    }

    public void setBoot(String boot) {
        this.boot = boot;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public int getSockets() {
        return sockets;
    }

    public void setSockets(int sockets) {
        this.sockets = sockets;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIde2() {
        return ide2;
    }

    public void setIde2(String ide2) {
        this.ide2 = ide2;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public int getNuma() {
        return numa;
    }

    public void setNuma(int numa) {
        this.numa = numa;
    }

    public int getBalloon() {
        return balloon;
    }

    public void setBalloon(int balloon) {
        this.balloon = balloon;
    }

    public String getScsihw() {
        return scsihw;
    }

    public void setScsihw(String scsihw) {
        this.scsihw = scsihw;
    }

    public String getOstype() {
        return ostype;
    }

    public void setOstype(String ostype) {
        this.ostype = ostype;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getScsi0() {
        return scsi0;
    }

    public void setScsi0(String scsi0) {
        this.scsi0 = scsi0;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getVmgenid() {
        return vmgenid;
    }

    public void setVmgenid(String vmgenid) {
        this.vmgenid = vmgenid;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getNet0() {
        return net0;
    }

    public void setNet0(String net0) {
        this.net0 = net0;
    }

    public String getSmbios1() {
        return smbios1;
    }

    public void setSmbios1(String smbios1) {
        this.smbios1 = smbios1;
    }

    public int getOnboot() {
        return onboot;
    }

    public void setOnboot(int onboot) {
        this.onboot = onboot;
    }

    @Override
    public String toString() {
        return "NodesQemuConfigResp{" +
                "boot='" + boot + '\'' +
                ", cores=" + cores +
                ", sockets=" + sockets +
                ", memory='" + memory + '\'' +
                ", name='" + name + '\'' +
                ", ide2='" + ide2 + '\'' +
                ", agent='" + agent + '\'' +
                ", numa=" + numa +
                ", balloon=" + balloon +
                ", scsihw='" + scsihw + '\'' +
                ", ostype='" + ostype + '\'' +
                ", meta='" + meta + '\'' +
                ", scsi0='" + scsi0 + '\'' +
                ", cpu='" + cpu + '\'' +
                ", vmgenid='" + vmgenid + '\'' +
                ", tags='" + tags + '\'' +
                ", digest='" + digest + '\'' +
                ", net0='" + net0 + '\'' +
                ", smbios1='" + smbios1 + '\'' +
                ", onboot=" + onboot +
                '}';
    }
}

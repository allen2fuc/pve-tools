package asia.chengfu.pve.service.req;

public class VMCloneReq {

    /**
     * 克隆的虚拟机名称前缀
     */
    private String cloneVmNamePrefix;

    /**
     * 克隆的数量
     */
    private int cloneNumber;

    public String getCloneVmNamePrefix() {
        return cloneVmNamePrefix;
    }

    public void setCloneVmNamePrefix(String cloneVmNamePrefix) {
        this.cloneVmNamePrefix = cloneVmNamePrefix;
    }

    public int getCloneNumber() {
        return cloneNumber;
    }

    public void setCloneNumber(int cloneNumber) {
        this.cloneNumber = cloneNumber;
    }

    @Override
    public String toString() {
        return "VMCloneReq{" +
                ", cloneVmNamePrefix='" + cloneVmNamePrefix + '\'' +
                ", cloneNumber=" + cloneNumber +
                '}';
    }
}

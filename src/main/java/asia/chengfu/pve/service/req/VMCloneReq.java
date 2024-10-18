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

    private int startIndex = 1;

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

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public String toString() {
        return "VMCloneReq{" +
                ", cloneVmNamePrefix='" + cloneVmNamePrefix + '\'' +
                ", cloneNumber=" + cloneNumber +
                '}';
    }
}

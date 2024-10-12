package asia.chengfu.pve.core.resp;

import cn.hutool.core.annotation.PropIgnore;


public class PVENodesQemuListResp implements Comparable<PVENodesQemuListResp> {
    /**
     * 唯一ID
     */
    private int vmid;

    /**
     * 名称
     */
    private String name;

    @PropIgnore
    private Long diskwrite;
    @PropIgnore
    private Double cpu;
    @PropIgnore
    private Long disk;

    /**
     * 多个tag,分号隔开
     */
    private String tags;
    @PropIgnore
    private Long diskread;
    private Long maxmem;
    /**
     * stopped、running
     */
    private String status;
    @PropIgnore
    private Long netout;
    @PropIgnore
    private Long netin;
    private Long maxdisk;
    private Long mem;
    @PropIgnore
    private Long pid;
    private Long cpus;
    private Long uptime;

    public int getVmid() {
        return vmid;
    }

    public void setVmid(int vmid) {
        this.vmid = vmid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDiskwrite() {
        return diskwrite;
    }

    public void setDiskwrite(Long diskwrite) {
        this.diskwrite = diskwrite;
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Long getDisk() {
        return disk;
    }

    public void setDisk(Long disk) {
        this.disk = disk;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getDiskread() {
        return diskread;
    }

    public void setDiskread(Long diskread) {
        this.diskread = diskread;
    }

    public Long getMaxmem() {
        return maxmem;
    }

    public void setMaxmem(Long maxmem) {
        this.maxmem = maxmem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getNetout() {
        return netout;
    }

    public void setNetout(Long netout) {
        this.netout = netout;
    }

    public Long getNetin() {
        return netin;
    }

    public void setNetin(Long netin) {
        this.netin = netin;
    }

    public Long getMaxdisk() {
        return maxdisk;
    }

    public void setMaxdisk(Long maxdisk) {
        this.maxdisk = maxdisk;
    }

    public Long getMem() {
        return mem;
    }

    public void setMem(Long mem) {
        this.mem = mem;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getCpus() {
        return cpus;
    }

    public void setCpus(Long cpus) {
        this.cpus = cpus;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    @Override
    public String toString() {
        return "NodesQemuListResp{" +
                "vmid=" + vmid +
                ", name='" + name + '\'' +
                ", diskwrite=" + diskwrite +
                ", cpu=" + cpu +
                ", disk=" + disk +
                ", tags='" + tags + '\'' +
                ", diskread=" + diskread +
                ", maxmem=" + maxmem +
                ", status='" + status + '\'' +
                ", netout=" + netout +
                ", netin=" + netin +
                ", maxdisk=" + maxdisk +
                ", mem=" + mem +
                ", pid=" + pid +
                ", cpus=" + cpus +
                ", uptime=" + uptime +
                '}';
    }

    @Override
    public int compareTo(PVENodesQemuListResp o) {
        int self = vmid;
        int that = o.getVmid();
        return self - that;
    }
}

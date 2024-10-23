package asia.chengfu.pve.service.resp;

import cn.hutool.core.util.StrUtil;

import java.util.Comparator;

public class VMListInfo implements Comparator<VMListInfo> {
    private int vmid;
    private String name;
    private String status;
    private String tags;

    private String ip;
    private String mac;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    @Override
    public String toString() {
        return "VMListInfo{" +
                "vmid=" + vmid +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", tags='" + tags + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }

    @Override
    public int compare(VMListInfo o1, VMListInfo o2) {
        return StrUtil.compareVersion(o1.getName(), o2.getName());
    }
}

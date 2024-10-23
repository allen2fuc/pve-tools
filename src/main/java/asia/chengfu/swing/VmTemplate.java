package asia.chengfu.swing;

public class VmTemplate {
    private int vmid;
    private String name;

    public VmTemplate(int vmid, String name) {
        this.vmid = vmid;
        this.name = name;
    }

    public int getVmid() {
        return vmid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // 组合框显示虚拟机名称
    }
}
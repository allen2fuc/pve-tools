package asia.chengfu.swing;

public class VirtualMachine {
    private int vmId;
    private String name;
    private String status;
    private String memory;
    private String cpu;
    private String disk;
    private String tags;

    public VirtualMachine(int vmId, String name, String status, String memory, String cpu, String disk, String tags) {
        this.vmId = vmId;
        this.name = name;
        this.status = status;
        this.memory = memory;
        this.cpu = cpu;
        this.disk = disk;
        this.tags = tags;
    }

    public int getVmId() {
        return vmId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTags() {
        return tags;
    }

    public String getMemory() {
        return memory;
    }

    public String getCpu() {
        return cpu;
    }

    public String getDisk() {
        return disk;
    }
}
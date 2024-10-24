package asia.chengfu.swing.bean;

public record VirtualMachine(int vmId, String name, String status, String memory, String cpu, String disk,
                             String tags) {
}
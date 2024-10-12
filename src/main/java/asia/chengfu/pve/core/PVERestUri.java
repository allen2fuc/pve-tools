package asia.chengfu.pve.core;

public enum PVERestUri {
    ACCESS_TICKET("/api2/json/access/ticket"),

    CLUSTER_OPTIONS("/api2/json/cluster/options"),
    CLUSTER_STATUS("/api2/json/cluster/status"),

    NODES_QEMU_LIST("/api2/json/nodes/{node}/qemu"),
    NODES_QEMU_DELETE("/api2/json/nodes/{node}/qemu/{vmid}"),
    NODES_QEMU_CLONE("/api2/json/nodes/{node}/qemu/{vmid}/clone"),
    NODES_QEMU_CONFIG("/api2/json/nodes/{node}/qemu/{vmid}/config"),
    NODES_QEMU_AGENT_SHUTDOWN("/api2/json/nodes/{node}/qemu/{vmid}/agent/shutdown"),

    NODES_QEMU_STATUS_START("/api2/json/nodes/{node}/qemu/{vmid}/status/start"),
    NODES_QEMU_STATUS_STOP("/api2/json/nodes/{node}/qemu/{vmid}/status/stop"),
    NODES_QEMU_STATUS_CURRENT("/api2/json/nodes/{node}/qemu/{vmid}/status/current"),

    NODES_QEMU_AGENT_PASSWORD("/api2/json/nodes/{node}/qemu/{vmid}/agent/set-user-password"),

    NODES_QEMU_AGENT_NETWORK("/api2/json/nodes/{node}/qemu/{vmid}/agent/network-get-interfaces");

    private String uri;

    PVERestUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

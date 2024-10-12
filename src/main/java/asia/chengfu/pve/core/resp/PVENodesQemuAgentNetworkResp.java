package asia.chengfu.pve.core.resp;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.util.StrUtil;

import java.util.List;

public class PVENodesQemuAgentNetworkResp {
    private List<QemuAgentNetworkGetInterfaceResult> result;

    public List<QemuAgentNetworkGetInterfaceResult> getResult() {
        return result;
    }

    public void setResult(List<QemuAgentNetworkGetInterfaceResult> result) {
        this.result = result;
    }

    public String fetchIpv4IPAddress(){
        for (QemuAgentNetworkGetInterfaceResult interfaceResult : getResult()) {
            for (QemuAgentNetworkGetInterfaceIpAddress ipAddress : interfaceResult.getIpAddresses()) {
                if (StrUtil.equals(ipAddress.getIpAddressType(), "ipv4")) {

                    if (!StrUtil.equals("127.0.0.1", ipAddress.getIpAddress())) {

                        return ipAddress.getIpAddress();
                    }
                }
            }
        }
        return null;
    }

    public static class QemuAgentNetworkGetInterfaceResult{

        private String name;

        @Alias("hardware-address")
        private String hardwareAddress;

        @Alias("ip-addresses")
        private List<QemuAgentNetworkGetInterfaceIpAddress> ipAddresses;

        private QemuAgentNetworkGetInterfaceStatistics statistics;

        public List<QemuAgentNetworkGetInterfaceIpAddress> getIpAddresses() {
            return ipAddresses;
        }

        public void setIpAddresses(List<QemuAgentNetworkGetInterfaceIpAddress> ipAddresses) {
            this.ipAddresses = ipAddresses;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHardwareAddress() {
            return hardwareAddress;
        }

        public void setHardwareAddress(String hardwareAddress) {
            this.hardwareAddress = hardwareAddress;
        }

        @Override
        public String toString() {
            return "QemuAgentNetworkGetInterfaceResult{" +
                    "name='" + name + '\'' +
                    ", hardwareAddress='" + hardwareAddress + '\'' +
                    ", ipAddresses=" + ipAddresses +
                    '}';
        }
    }

    public static class QemuAgentNetworkGetInterfaceStatistics{
        @Alias("tx-errs")
        private int txErrs;
        @Alias("rx-errs")
        private int rxErrs;
        @Alias("tx-dropped")
        private int txDropped;
        @Alias("rx-dropped")
        private int rxDropped;
        @Alias("tx-bytes")
        private long txBytes;
        @Alias("rx-bytes")
        private long rxBytes;
        @Alias("rx-packets")
        private long rxPackets;
        @Alias("tx-packets")
        private long txPackets;

        public int getTxErrs() {
            return txErrs;
        }

        public void setTxErrs(int txErrs) {
            this.txErrs = txErrs;
        }

        public int getRxErrs() {
            return rxErrs;
        }

        public void setRxErrs(int rxErrs) {
            this.rxErrs = rxErrs;
        }

        public int getTxDropped() {
            return txDropped;
        }

        public void setTxDropped(int txDropped) {
            this.txDropped = txDropped;
        }

        public int getRxDropped() {
            return rxDropped;
        }

        public void setRxDropped(int rxDropped) {
            this.rxDropped = rxDropped;
        }

        public long getTxBytes() {
            return txBytes;
        }

        public void setTxBytes(long txBytes) {
            this.txBytes = txBytes;
        }

        public long getRxBytes() {
            return rxBytes;
        }

        public void setRxBytes(long rxBytes) {
            this.rxBytes = rxBytes;
        }

        public long getRxPackets() {
            return rxPackets;
        }

        public void setRxPackets(long rxPackets) {
            this.rxPackets = rxPackets;
        }

        public long getTxPackets() {
            return txPackets;
        }

        public void setTxPackets(long txPackets) {
            this.txPackets = txPackets;
        }

        @Override
        public String toString() {
            return "QemuAgentNetworkGetInterfaceStatistics{" +
                    "txErrs=" + txErrs +
                    ", rxErrs=" + rxErrs +
                    ", txDropped=" + txDropped +
                    ", rxDropped=" + rxDropped +
                    ", txBytes=" + txBytes +
                    ", rxBytes=" + rxBytes +
                    ", rxPackets=" + rxPackets +
                    ", txPackets=" + txPackets +
                    '}';
        }
    }

    public static class QemuAgentNetworkGetInterfaceIpAddress{
        @Alias("ip-address-type")
        private String ipAddressType;

        @Alias("ip-address")
        private String ipAddress;

        private Integer prefix;

        public String getIpAddressType() {
            return ipAddressType;
        }

        public void setIpAddressType(String ipAddressType) {
            this.ipAddressType = ipAddressType;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public Integer getPrefix() {
            return prefix;
        }

        public void setPrefix(Integer prefix) {
            this.prefix = prefix;
        }

        @Override
        public String toString() {
            return "QemuAgentNetworkGetInterfaceIpAddress{" +
                    "ipAddressType='" + ipAddressType + '\'' +
                    ", ipAddress='" + ipAddress + '\'' +
                    ", prefix=" + prefix +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NodesQemuAgentNetworkResp{" +
                "result=" + result +
                '}';
    }
}

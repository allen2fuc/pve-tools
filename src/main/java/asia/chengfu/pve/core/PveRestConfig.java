package asia.chengfu.pve.core;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

public class PveRestConfig {
    private String address;
    private String username;
    private String password;

    public PveRestConfig(String address, String username, String password) {
        this.address = address;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getAddress() {
        return address;
    }

    public static PveRestConfigBuilder builder() {
        return new PveRestConfigBuilder();
    }

    public static class PveRestConfigBuilder {
        private String host;
        private Integer port;
        private String username;
        private String password;

        public PveRestConfigBuilder host(String host) {
            this.host = host;
            return this;
        }

        public PveRestConfigBuilder port(int port) {
            this.port = port;
            return this;
        }

        public PveRestConfigBuilder username(String username) {
            this.username = username;
            return this;
        }

        public PveRestConfigBuilder password(String password) {
            this.password = password;
            return this;
        }

        public PveRestConfig build() {
            String actualHost = StrUtil.nullToDefault(host, "127.0.0.1");
            int actualPort = ObjectUtil.defaultIfNull(port, 8006);
            String address = StrUtil.format("https://{}:{}", actualHost, actualPort);
            return new PveRestConfig(address, username, password);
        }
    }

}

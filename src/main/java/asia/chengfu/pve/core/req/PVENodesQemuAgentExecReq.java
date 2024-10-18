package asia.chengfu.pve.core.req;

import cn.hutool.core.annotation.Alias;

public class PVENodesQemuAgentExecReq {
    private String command;

    @Alias("input-data")
    private String inputData;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    @Override
    public String toString() {
        return "PVENodesQemuAgentExecReq{" +
                "command='" + command + '\'' +
                ", inputData='" + inputData + '\'' +
                '}';
    }
}

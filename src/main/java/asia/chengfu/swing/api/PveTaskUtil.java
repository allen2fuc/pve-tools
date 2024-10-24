package asia.chengfu.swing.api;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.Timer;
import java.util.TimerTask;

public class PveTaskUtil {
    private static final int POLLING_INTERVAL = 1000; // 5秒轮询一次


    /**
     * 查询任务状态并更新进度条
     *
     * @param nodeName 节点名称
     * @param upid     任务的UPID
     */
    public static void trackTaskProgress(VMOperations vmOperations, String nodeName, String upid, VMAction vmAction, TaskProgressListener listener) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CatchUtils.catchRun(() -> {
                    JSONObject statusResponse = getTaskStatus(vmOperations, nodeName, upid);
                    Assert.notNull(statusResponse, "错误的响应");

                    TaskStatus taskStatus = statusResponse.toBean(TaskStatus.class);

                    if (StrUtil.equals(taskStatus.getStatus(), "running")) {
                        listener.onProgress(getOperationBegins(vmAction));
                        // 解析日志中的进度
                        parseProgressFromLog(vmOperations, nodeName, upid);
                    }

                    if (StrUtil.equals(taskStatus.getStatus(), "stopped")) {
                        listener.onComplete(getOperationEnds(vmAction));
                        timer.cancel();
                    }
                }, e -> {
                    listener.onError(e);
                    timer.cancel();
                });
            }
        }, 0, POLLING_INTERVAL);
    }

    /**
     * 获取任务的当前状态
     *
     * @param nodeName 节点名称
     * @param upid     任务的UPID
     * @return 任务状态的JSON响应
     */
    private static JSONObject getTaskStatus(VMOperations vmOperations, String nodeName, String upid) {
        // 这里假设有一个 vmOperations 类来处理 PVE API 请求
        return vmOperations.getTaskStatus(nodeName, upid);
    }

    /**
     * 解析任务日志，提取进度信息
     *
     * @param nodeName 节点名称
     * @param upid     任务的UPID
     */
    private static void parseProgressFromLog(VMOperations vmOperations, String nodeName, String upid) {
//        JSONArray logEntries = vmOperations.getTaskLog(nodeName, upid);
//        logger.info("任务日志：{}", logEntries);
    }


    private static String getOperationBegins(VMAction vmAction) {
        return switch (vmAction) {
            case START -> "starting";
            case STOP -> "stopping";
            case DELETE -> "deleting";
            case RESTART -> "restarting";
        };
    }

    private static String getOperationEnds(VMAction vmAction) {
        return switch (vmAction) {
            case DELETE -> "deleted";
            case STOP -> "stopped";
            default -> "running";
        };
    }


    @Data
    private static class TaskStatus {
        private String id;
        private int pid;
        private String upid;
        private String type;
        private String node;
        private String user;
        private int starttime;
        private String status;
        private int pstart;
    }
}

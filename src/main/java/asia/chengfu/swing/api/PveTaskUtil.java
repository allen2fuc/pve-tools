package asia.chengfu.swing.api;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class PveTaskUtil {
    private static final int POLLING_INTERVAL = 1000; // 5秒轮询一次
    // 次数不能超过30次
    private static final int MAX_POLLING_COUNT = 30;


    /**
     * 查询任务状态并更新进度条
     *
     * @param nodeName 节点名称
     * @param upid     任务的UPID
     */
    public static void trackTaskProgress(VMOperations vmOperations, String nodeName, String upid, VMAction vmAction, TaskProgressListener listener) {
        Timer timer = new Timer();

        AtomicInteger pollingCount = new AtomicInteger(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CatchUtils.catchRun(() -> {
                    JSONObject statusResponse = getTaskStatus(vmOperations, nodeName, upid);
                    Assert.notNull(statusResponse, "错误的响应");

                    TaskStatus taskStatus = statusResponse.toBean(TaskStatus.class);

                    if (StrUtil.equals(taskStatus.getStatus(), "running")) {
                        // 解析日志中的进度
                        parseProgressFromLog(vmOperations, nodeName, upid);
                        listener.onProgress(vmAction);
                    }else if (StrUtil.equals(taskStatus.getStatus(), "stopped")) {
                        listener.onComplete(vmAction);
                        timer.cancel();
                    }else if (pollingCount.incrementAndGet() >= MAX_POLLING_COUNT) {
                        listener.onError(new RuntimeException("任务超时"));
                        timer.cancel();
                    }

                }, e -> {
                    timer.cancel();
                    listener.onError(e);
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
        JSONArray logEntries = vmOperations.getTaskLog(nodeName, upid);
        log.info("任务日志：{}", logEntries);
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

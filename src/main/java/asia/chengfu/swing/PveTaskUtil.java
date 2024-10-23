package asia.chengfu.swing;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class PveTaskUtil {
    private static final Logger logger = LoggerFactory.getLogger(PveTaskUtil.class);

    private static final int POLLING_INTERVAL = 1000; // 5秒轮询一次


    /**
     * 查询任务状态并更新进度条
     *
     * @param nodeName 节点名称
     * @param upid     任务的UPID
     */
    public static void trackTaskProgress(VMOperations vmOperations, String nodeName, String upid, int operator, TaskProgressListener listener) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    JSONObject statusResponse = getTaskStatus(vmOperations, nodeName, upid);
                    Assert.state(statusResponse != null, "错误的响应");

                    TaskStatus taskStatus = statusResponse.toBean(TaskStatus.class);

                    if (StrUtil.equals(taskStatus.getStatus(), "running")) {
                        listener.onProgress(getOperationBegins(operator));
                        // 解析日志中的进度
                        parseProgressFromLog(vmOperations, nodeName, upid);
                    }

                    if (StrUtil.equals(taskStatus.getStatus(), "stopped")) {
                        listener.onComplete(getOperationEnds(operator));
                        timer.cancel();
                    }
                } catch (Exception e) {
                    listener.onError(e);
                    timer.cancel();
                }
            }
        }, 0, POLLING_INTERVAL);
    }

    /**
     * 获取任务的当前状态
     *
     * @param vmOperations
     * @param nodeName     节点名称
     * @param upid         任务的UPID
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
     * @return 提取的进度（百分比）
     */
    private static void parseProgressFromLog(VMOperations vmOperations, String nodeName, String upid) {
//        JSONArray logEntries = vmOperations.getTaskLog(nodeName, upid);
//        logger.info("任务日志：{}", logEntries);
    }


    private static String getOperationBegins(int operator) {
        switch (operator) {
            case VMOperations.START:
                return "starting";
            case VMOperations.STOP:
                return "stopping";
            case VMOperations.DELETE:
                return "deleting";
            case VMOperations.RESTART:
                return "restarting";
            default:
                return "running";
        }
    }

    private static String getOperationEnds(int operator) {
        switch (operator) {
            case VMOperations.DELETE:
                return "deleted";
            case VMOperations.STOP:
                return "stopped";
            default:
                return "running";
        }
    }


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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getUpid() {
            return upid;
        }

        public void setUpid(String upid) {
            this.upid = upid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public int getStarttime() {
            return starttime;
        }

        public void setStarttime(int starttime) {
            this.starttime = starttime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getPstart() {
            return pstart;
        }

        public void setPstart(int pstart) {
            this.pstart = pstart;
        }

        @Override
        public String toString() {
            return "TaskStatus{" +
                    "id='" + id + '\'' +
                    ", pid=" + pid +
                    ", upid='" + upid + '\'' +
                    ", type='" + type + '\'' +
                    ", node='" + node + '\'' +
                    ", user='" + user + '\'' +
                    ", starttime=" + starttime +
                    ", status='" + status + '\'' +
                    ", pstart=" + pstart +
                    '}';
        }
    }
}

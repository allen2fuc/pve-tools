package asia.chengfu.swing.api;


import java.util.function.Consumer;

public class CatchUtils {

    public static void catchRun(Runnable runnable) {
        catchRun(runnable, null, null);
    }

    // 执行操作，如果有异常只记录日志，不抛出
    public static void catchRun(Runnable runnable, Consumer<Exception> errorHandler) {
        catchRun(runnable, errorHandler, null);
    }

    public static void catchRun(Runnable runnable, Consumer<Exception> errorHandler, Runnable finallyRunnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        } finally {
            if (finallyRunnable != null) {
                finallyRunnable.run();
            }
        }
    }
}

package asia.chengfu.swing;


import java.util.function.Consumer;

public class CatchUtils {

    // 执行操作，如果有异常只记录日志，不抛出
    public static void catchRun(Runnable runnable, Consumer<Exception> errorHandler) {
        try {
            runnable.run();
        } catch (Exception e) {
            errorHandler.accept(e);
        }
    }
}

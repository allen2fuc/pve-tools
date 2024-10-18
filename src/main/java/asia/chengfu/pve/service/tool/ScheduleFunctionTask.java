package asia.chengfu.pve.service.tool;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.StaticLog;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ScheduleFunctionTask<T> {
    private Supplier<T> supplier;
    private Predicate<T> predicate;
    private T output;

    public ScheduleFunctionTask(Supplier<T> supplier, Predicate<T> predicate) {
        this.supplier = supplier;
        this.predicate = predicate;
    }

    public void scheduleJoin(int initDelay, int interval, TimeUnit unit) {
        ScheduledThreadPoolExecutor scheduledExecutor = ThreadUtil.createScheduledExecutor(1);
        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(1);
        ScheduledFuture<?> schedule = scheduledExecutor.scheduleAtFixedRate(() -> {
            try{
                final T transformer = supplier.get();

                StaticLog.debug("transformer: {}", transformer);

                if (predicate.test(transformer)) {
                    setOutput(transformer);
                    countDownLatch.countDown();
                }
            } catch (Exception e) {
                StaticLog.error(e);
            }
        }, initDelay, interval, unit);

        try{
            countDownLatch.await();
            schedule.cancel(true);
            scheduledExecutor.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOutput(T output) {
        this.output = output;
    }

    public T getOutput() {
        return output;
    }
}

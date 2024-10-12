package asia.chengfu.pve.service;

import asia.chengfu.pve.core.PVECache;
import asia.chengfu.pve.core.exception.HttpStatusException;
import asia.chengfu.pve.service.impl.PVEServiceImpl;
import cn.hutool.log.StaticLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class PVEServiceProxy implements InvocationHandler {

    private final AtomicInteger retryNum = new AtomicInteger();
    private final PVEService pveService = new PVEServiceImpl();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(pveService, args);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException invocationTargetException) {
                Throwable targetException = invocationTargetException.getTargetException();
                if (targetException instanceof HttpStatusException httpStatusException) {
                    status401AndRetry(httpStatusException, proxy, method, args);
                }
            }
            throw e;
        }
    }

    private Object status401AndRetry(HttpStatusException httpStatusException, Object proxy, Method method, Object[] args) throws Throwable {
        int increment = retryNum.getAndIncrement();
        if (httpStatusException.getStatus() == 401 && increment < 3) {
            StaticLog.debug("Retry: {}", increment + 1);
            PVECache.openRefreshFlag();
            return invoke(proxy, method, args);
        }
        retryNum.set(0);
        throw httpStatusException;
    }
}

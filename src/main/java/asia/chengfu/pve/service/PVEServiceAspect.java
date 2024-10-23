package asia.chengfu.pve.service;

import asia.chengfu.pve.core.PVECache;
import asia.chengfu.pve.core.exception.HttpStatusException;
import asia.chengfu.pve.service.impl.PVEServiceImpl;
import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.Aspect;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.StaticLog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class PVEServiceAspect implements Aspect {

    private final AtomicInteger retryNum = new AtomicInteger();

    private Object statusAndRetry(HttpStatusException httpStatusException, Object proxy, Method method, Object[] args) {
        int increment = retryNum.getAndIncrement();
        if ((httpStatusException.getStatus() == 401)
                && increment < 3) {
            StaticLog.debug("401 Retry: {}", increment + 1);
            PVECache.openRefreshFlag();
            return before(proxy, method, args);
        } else if (httpStatusException.getStatus() == 500 && increment < 3) {
            ThreadUtil.sleep(3000);
            StaticLog.debug("500 Retry: {}", increment + 1);
            return before(proxy, method, args);
        }
        retryNum.set(0);
        throw httpStatusException;
    }

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        return true;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        if (e instanceof InvocationTargetException invocationTargetException) {
            Throwable targetException = invocationTargetException.getTargetException();
            if (targetException instanceof HttpStatusException httpStatusException) {
                statusAndRetry(httpStatusException, target, method, args);
            }
        }
        return false;
    }
}

package asia.chengfu.pve.service;

import asia.chengfu.pve.service.impl.PVEServiceImpl;
import cn.hutool.aop.ProxyUtil;

public class PVEServiceInstance {
    private static PVEService INSTANCE =  getPVEServiceProxy();
    public static PVEService get() {
        return INSTANCE;
    }

    private static PVEService getPVEServiceProxy() {
        PVEService pveService = new PVEServiceImpl("10.0.0.85", "root@pam", "Vst123");
        PVEServiceAspect aspect = new PVEServiceAspect();
        PVEService service = ProxyUtil.proxy(pveService, aspect);
        return service;
    }
}

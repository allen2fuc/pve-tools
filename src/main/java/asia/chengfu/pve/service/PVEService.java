package asia.chengfu.pve.service;

import asia.chengfu.pve.service.req.VMCloneReq;

import java.util.List;

public interface PVEService {

    void deleteVm(String node, String... vmids);

    void stopVm(String node, String... vmids);

    List<String> cloneVm(String node, String vmid, VMCloneReq req);
}

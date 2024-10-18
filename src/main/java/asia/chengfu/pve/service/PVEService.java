package asia.chengfu.pve.service;

import asia.chengfu.pve.service.req.AddAccessAclReq;
import asia.chengfu.pve.service.req.VMCloneReq;
import asia.chengfu.pve.service.resp.VMListInfo;

import java.util.List;

public interface PVEService {

    void deleteVm(String node, String... vmids);

    void addAccessAcl(AddAccessAclReq... reqs);

    void postPlaceIPToTag(String node, String prefix, String... vmids);

    void appendTag(String node, String vmid, String tag);

    void stopVm(String node, String... vmids);

    void rebootVm(String node, String... vmids);

    List<String> cloneVm(String node, String vmid, VMCloneReq req);

    void addVmDomain(String node, String vmid, String domainName, String adminPassword);

    List<VMListInfo> listVm(String node, String vmNamePrefix);

    void generateRDPFile(String username, String ip, String rdpFile);
}

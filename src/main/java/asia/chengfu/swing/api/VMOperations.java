package asia.chengfu.swing.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class VMOperations {

    private static final String TICKET_KEY = "PVEAuthCookie";
    private static final String LOGIN_URL = "/api2/json/access/ticket";
    private static final String NODES_URL = "/api2/json/nodes";
    private static final String VMS_URL = "/qemu";
    private static final String STATUS_URL = "/status";
    private static final String CLONE_URL = "/clone";
    private static final String CONFIG_URL = "/config";
    private static final String NETWORK_GET_INTERFACES_URL = "/agent/network-get-interfaces";
    private static final String TASKS_URL = "/tasks";
    private static final String LOG_URL = "/log";

    private final String rootUrl;
    private String ticket;
    private String csrfPreventionToken;

    public VMOperations(String rootUrl, String username, String password) {
        this.rootUrl = rootUrl;
        login(username, password);
    }

    private void login(String username, String password) {
        String loginUrl = rootUrl + LOGIN_URL;
        HttpResponse response = HttpRequest.post(loginUrl)
                .form("username", username)
                .form("password", password)
                .execute();

        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        ticket = jsonResponse.getJSONObject("data").getStr("ticket");
        csrfPreventionToken = jsonResponse.getJSONObject("data").getStr("CSRFPreventionToken");
    }

    public List<String> fetchNodes() {
        String nodesUrl = rootUrl + NODES_URL;
        HttpResponse response = sendGetRequest(nodesUrl);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        JSONArray nodesArray = jsonResponse.getJSONArray("data");
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < nodesArray.size(); i++) {
            JSONObject node = nodesArray.getJSONObject(i);
            nodes.add(node.getStr("node"));
        }
        return nodes;
    }

    public String startVM(int vmId, String nodeName) {
        return executeVmAction(vmId, nodeName, VMAction.START);
    }

    public String stopVM(int vmId, String nodeName) {
        return executeVmAction(vmId, nodeName, VMAction.STOP);
    }

    public String restartVM(int vmId, String nodeName) {
        return executeVmAction(vmId, nodeName, VMAction.RESTART);
    }

    public String deleteVM(int vmId, String nodeName) {
        String url = buildVmUrl(nodeName, vmId);
        HttpResponse response = ApiUtils.sendDeleteRequest(url, getCookie(), csrfPreventionToken);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getStr("data");
    }

    public int getNextAvailableVmid(String nodeName) {
        List<Integer> existingVmidList = fetchExistingVmidList(nodeName);
        return findNextAvailableVmid(existingVmidList);
    }

    private List<Integer> fetchExistingVmidList(String nodeName) {
        List<Integer> vmidList = new ArrayList<>();
        JSONArray data = fetchVirtualMachinesFromAPI(nodeName);
        for (int i = 0; i < data.size(); i++) {
            JSONObject vm = data.getJSONObject(i);
            int vmid = vm.getInt("vmid");
            vmidList.add(vmid);
        }
        return vmidList;
    }

    private int findNextAvailableVmid(List<Integer> existingVmidList) {
        if (existingVmidList.isEmpty()) {
            return 100; // 默认从100开始
        }
        Collections.sort(existingVmidList);
        for (int i = 0; i < existingVmidList.size() - 1; i++) {
            if (existingVmidList.get(i) + 1 != existingVmidList.get(i + 1)) {
                return existingVmidList.get(i) + 1;
            }
        }
        return existingVmidList.get(existingVmidList.size() - 1) + 1;
    }

    public JSONArray fetchVirtualMachinesFromAPI(String nodeName) {
        String vmListUrl = buildNodeUrl(nodeName) + VMS_URL;
        HttpResponse response = sendGetRequest(vmListUrl);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONArray("data");
    }

    public void cloneVM(int templateId, int newVmid, String newName, String nodeName) {
        String url = buildNodeUrl(nodeName) + VMS_URL + "/" + templateId + CLONE_URL;
        JSONObject body = JSONUtil.createObj()
                .set("newid", newVmid)
                .set("name", newName);
        HttpResponse response = ApiUtils.sendPostRequest(url, getCookie(), csrfPreventionToken, body);
        ApiResponseParser.parseResponse(response);
    }

    public void updateVMConfiguration(int vmId, String nodeName, JSONObject config) {
        String url = buildVmUrl(nodeName, vmId) + CONFIG_URL;
        HttpResponse response = ApiUtils.sendPutRequest(url, getCookie(), csrfPreventionToken, config);
        ApiResponseParser.parseResponse(response);
    }

    public JSONObject fetchVmStatus(String nodeName, int vmId) {
        String url = buildVmUrl(nodeName, vmId) + STATUS_URL + "/current";
        HttpResponse response = sendGetRequest(url);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONObject("data");
    }

    public JSONObject fetchVmNetworkInterfaces(String nodeName, int vmId) {
        String url = buildVmUrl(nodeName, vmId) + NETWORK_GET_INTERFACES_URL;
        HttpResponse response = sendGetRequest(url);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONObject("data");
    }

    public JSONObject getTaskStatus(String nodeName, String upid) {
        String url = buildNodeUrl(nodeName) + TASKS_URL + "/" + upid + STATUS_URL;
        HttpResponse response = sendGetRequest(url);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONObject("data");
    }

    public JSONArray getTaskLog(String nodeName, String upid) {
        String url = buildNodeUrl(nodeName) + TASKS_URL + "/" + upid + LOG_URL;
        HttpResponse response = sendGetRequest(url);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONArray("data");
    }

    private String buildNodeUrl(String nodeName) {
        return rootUrl + NODES_URL + "/" + nodeName;
    }

    private String buildVmUrl(String nodeName, int vmId) {
        return buildNodeUrl(nodeName) + VMS_URL + "/" + vmId;
    }

    private String getCookie() {
        return TICKET_KEY + "=" + ticket;
    }

    private HttpResponse sendGetRequest(String url) {
        return ApiUtils.sendGetRequest(url, getCookie(), csrfPreventionToken);
    }

    private String executeVmAction(int vmId, String nodeName, VMAction action) {
        String url = buildVmUrl(nodeName, vmId) + STATUS_URL + "/" + action.name().toLowerCase();
        HttpResponse response = ApiUtils.sendPostRequest(url, getCookie(), csrfPreventionToken, new JSONObject());
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getStr("data");
    }
}
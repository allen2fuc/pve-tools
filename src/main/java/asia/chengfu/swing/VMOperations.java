package asia.chengfu.swing;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VMOperations {

    public static final int RUNNING = -1;
    public static final int START = 1;
    public static final int STOP = 2;
    public static final int DELETE = 3;
    public static final int RESTART = 4;

    private final String rootUrl;
    private String ticket;
    private String csrfPreventionToken;

    public VMOperations(String rootUrl, String username, String password) {
        this.rootUrl = rootUrl;
        login(username, password);
    }

    private void login(String username, String password) {
        String loginUrl = rootUrl + "/api2/json/access/ticket";
        HttpResponse response = HttpRequest.post(loginUrl)
                .form("username", username)
                .form("password", password)
                .execute();

        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        ticket = jsonResponse.getJSONObject("data").getStr("ticket");
        csrfPreventionToken = jsonResponse.getJSONObject("data").getStr("CSRFPreventionToken");
    }

    public List<String> fetchNodes() {
        String nodesUrl = rootUrl + "/api2/json/nodes";
        HttpResponse response = ApiUtils.sendGetRequest(nodesUrl, getCookie(ticket), csrfPreventionToken);

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
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu/" + vmId + "/status/start";
        HttpResponse response = ApiUtils.sendPostRequest(url, getCookie(ticket), csrfPreventionToken, new JSONObject());
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getStr("data");

    }

    public String stopVM(int vmId, String nodeName) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu/" + vmId + "/status/stop";
        HttpResponse response = ApiUtils.sendPostRequest(url, getCookie(ticket), csrfPreventionToken, new JSONObject());
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getStr("data");
    }

    public String restartVM(int vmId, String nodeName) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu/" + vmId + "/status/reboot";
        HttpResponse response = ApiUtils.sendPostRequest(url, getCookie(ticket), csrfPreventionToken, new JSONObject());
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getStr("data");
    }

    public String deleteVM(int vmId, String nodeName) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu/" + vmId;
        HttpResponse response = ApiUtils.sendDeleteRequest(url, getCookie(ticket), csrfPreventionToken);
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
        return existingVmidList.getLast() + 1;
    }

    public JSONArray fetchVirtualMachinesFromAPI(String nodeName) {
        String vmListUrl = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu";
        HttpResponse response = ApiUtils.sendGetRequest(vmListUrl, getCookie(ticket), csrfPreventionToken);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONArray("data");
    }


    public void cloneVM(int templateId, int newVmid, String newName, String nodeName) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu/" + templateId + "/clone";
        JSONObject body = JSONUtil.createObj()
                .set("newid", newVmid)
                .set("name", newName);
        HttpResponse response = ApiUtils.sendPostRequest(url, getCookie(ticket), csrfPreventionToken, body);
        ApiResponseParser.parseResponse(response);
    }

    public void updateVMConfiguration(int vmId, String nodeName, JSONObject config) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu/" + vmId + "/config";
        HttpResponse response = ApiUtils.sendPutRequest(url, getCookie(ticket), csrfPreventionToken, config);
        ApiResponseParser.parseResponse(response);
    }

    public JSONObject fetchVmStatus(String nodeName, int vmId) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu/" + vmId + "/status/current";
        HttpResponse response = ApiUtils.sendGetRequest(url, getCookie(ticket), csrfPreventionToken);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONObject("data");
    }

    public JSONObject fetchVmNetworkInterfaces(String nodeName, int vmId) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/qemu/" + vmId + "/agent/network-get-interfaces";
        HttpResponse response = ApiUtils.sendGetRequest(url, getCookie(ticket), csrfPreventionToken);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONObject("data");
    }

    public JSONObject getTaskStatus(String nodeName, String upid) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/tasks/" + upid + "/status";
        HttpResponse response = ApiUtils.sendGetRequest(url, getCookie(ticket), csrfPreventionToken);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONObject("data");
    }

    public JSONArray getTaskLog(String nodeName, String upid) {
        String url = rootUrl + "/api2/json/nodes/" + nodeName + "/tasks/" + upid + "/log";
        HttpResponse response = ApiUtils.sendGetRequest(url, getCookie(ticket), csrfPreventionToken);
        JSONObject jsonResponse = ApiResponseParser.parseResponse(response);
        return jsonResponse.getJSONArray("data");
    }
    
    private String getCookie(String ticket){
        return "PVEAuthCookie=" + ticket;
    }
}
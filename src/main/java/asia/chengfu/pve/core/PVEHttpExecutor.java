package asia.chengfu.pve.core;

import asia.chengfu.pve.core.exception.HttpStatusException;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;

import java.util.HashMap;

class PVEHttpExecutor {

    private static final String DATA_KEY = "data";

    public static <T> T execute(String node, String vmid, PVEHttpParam<T> param, PveRestConfig config) {

        Method method = param.getMethod();
        String url = getUrl(node, vmid, param.getRestUri(), config);
        String formMap = param.getFormMap();

        HttpRequest req = HttpUtil.createRequest(method, url);

        if (ObjectUtil.isNotEmpty(formMap)) {
            req.body(formMap, ContentType.JSON.getValue());
        }

        req.headerMap(param.getHeaders(), true);

        HttpResponse resp = req.execute();

        String respContent = resp.body();

        StaticLog.debug("URI:【{}】, Status: [{}], Resp:【{}】", url, resp.getStatus(), respContent);

        if (resp.isOk()) {

            JSONObject jsonObject = JSONUtil.parseObj(respContent);

            Object content = jsonObject.get(DATA_KEY);

            return Convert.convert(param.getRespClazz(), content);

        } else {
            throw new HttpStatusException(url, resp.getStatus());
        }
    }

    private static String getUrl(String node, String vmid, PVERestUri restUri, PveRestConfig config) {
        String uriTemplate = restUri.getUri();

        HashMap<String, String> uriParam = new HashMap<>();

        String address = config.getAddress();

        if (StrUtil.isNotBlank(node)) {
            uriParam.put("node", node);
        }
        if (StrUtil.isNotBlank(vmid)) {
            uriParam.put("vmid", vmid);
        }

        String uri = StrUtil.format(uriTemplate, uriParam);
        return address + uri;
    }
}

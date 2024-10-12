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

public class PVEHttpExecutor {

    private static final String DATA_KEY = "data";


    public static <T> T execute(PVEHttpParam<T> param){

        Method method = param.getMethod();
        String url = getUrl(param.getRestUri());
        String formMap = param.getFormMap();

        HttpRequest req = HttpUtil.createRequest(method, url);

        if (ObjectUtil.isNotEmpty(formMap)) {
            req.body(formMap, ContentType.JSON.getValue());
        }

        req.headerMap(param.getHeaders(), true);

        HttpResponse resp = req.execute();

        String respContent = resp.body();

        StaticLog.debug("URI:【{}】, Resp:【{}】", url, respContent);

        if (resp.isOk()) {

            JSONObject jsonObject = JSONUtil.parseObj(respContent);

            Object content = jsonObject.get(DATA_KEY);

            return Convert.convert(param.getRespClazz(), content);

        }else {
            throw new HttpStatusException(url, resp.getStatus());
        }
    }

    private static String getUrl(PVERestUri restUri){
        String uriTemplate = restUri.getUri();

        HashMap<String, String> uriParam = new HashMap<>();

        String address = PVECache.getPveAddress();
        String currentNode = PVECache.getCurrentNode();
        String currentVmid = PVECache.getCurrentVmid();

        if (StrUtil.isNotBlank(currentNode)){
            uriParam.put("node", currentNode);
        }
        if (StrUtil.isNotBlank(currentVmid)){
            uriParam.put("vmid", currentVmid);
        }

        String uri = StrUtil.format(uriTemplate, uriParam);
        return address + uri;
    }
}

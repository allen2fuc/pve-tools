package asia.chengfu.pve.core;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

class PVEHttpParam<T> {

    private PVERestUri restUri;

    private Method method;

    private Map<String, String> headers;

    private String formMap;

    private TypeReference<T> respClazz;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getFormMap() {
        return formMap;
    }

    public void setFormMap(String formMap) {
        this.formMap = formMap;
    }

    public PVERestUri getRestUri() {
        return restUri;
    }

    public void setRestUri(PVERestUri restUri) {
        this.restUri = restUri;
    }

    public TypeReference<T> getRespClazz() {
        return respClazz;
    }

    public void setRespClazz(TypeReference<T> respClazz) {
        this.respClazz = respClazz;
    }

    public static <Req, Resp> PVEHttpParamBuilder<Req, Resp> builder() {
        return new PVEHttpParamBuilder<Req, Resp>();
    }

    public static class PVEHttpParamBuilder<Req, Resp> {
        private PVERestUri restUri;
        private Method method;
        private Map<String, String> headers;
        private Req req;
        private TypeReference<Resp> respClazz;

        public PVEHttpParamBuilder<Req, Resp> restUri(PVERestUri restUri) {
            this.restUri = restUri;
            return this;
        }

        public PVEHttpParamBuilder<Req, Resp> method(Method method) {
            this.method = method;
            return this;
        }

        public PVEHttpParamBuilder<Req, Resp> headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public PVEHttpParamBuilder<Req, Resp> req(Req req) {
            this.req = req;
            return this;
        }

        public PVEHttpParamBuilder<Req, Resp> respClazz(TypeReference<Resp> respClazz) {
            this.respClazz = respClazz;
            return this;
        }

        public PVEHttpParam<Resp> build() {
            PVEHttpParam<Resp> httpParam = new PVEHttpParam<>();
            httpParam.setMethod(method);
            httpParam.setFormMap(JSONUtil.toJsonStr(req));
            httpParam.setRestUri(restUri);
            httpParam.setRespClazz(respClazz);

            if (ObjUtil.isNotEmpty(this.headers)) {
                httpParam.setHeaders(headers);
            } else {
                String cookie = PVECache.getCookie();
                String token = PVECache.getToken();

                HashMap<String, String> headers = new HashMap<>();
                if (StrUtil.isNotBlank(cookie)) {
                    headers.put("Cookie", "PVEAuthCookie=" + cookie);
                }
                if (StrUtil.isNotBlank(token)) {
                    headers.put("CSRFPreventionToken", token);
                }
                httpParam.setHeaders(headers);
            }

            return httpParam;
        }


    }
}

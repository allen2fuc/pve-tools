package asia.chengfu.swing;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;


public class ApiUtils {

    public static HttpResponse sendGetRequest(String url, String cookie, String csrfToken) {
        return HttpRequest.get(url)
                .header("Cookie", cookie)
                .header("CSRFPreventionToken", csrfToken)
                .execute();
    }

    public static HttpResponse sendPostRequest(String url, String cookie, String csrfToken, JSONObject body) {
        return HttpRequest.post(url)
                .header("Cookie", cookie)
                .header("CSRFPreventionToken", csrfToken)
                .body(body.toString())
                .execute();
    }

    public static HttpResponse sendPutRequest(String url, String cookie, String csrfToken, JSONObject body) {
        return HttpRequest.put(url)
                .header("Cookie", cookie)
                .header("CSRFPreventionToken", csrfToken)
                .body(body.toString())
                .execute();
    }

    public static HttpResponse sendDeleteRequest(String url, String cookie, String csrfToken) {
        return HttpRequest.delete(url)
                .header("Cookie", cookie)
                .header("CSRFPreventionToken", csrfToken)
                .execute();
    }
}
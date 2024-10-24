package asia.chengfu.swing.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;

public class ApiUtils {

    private static final String COOKIE_HEADER = "Cookie";
    private static final String CSRF_TOKEN_HEADER = "CSRFPreventionToken";

    public static HttpResponse sendGetRequest(String url, String cookie, String csrfToken) {
        return sendRequest(HttpRequest.get(url), cookie, csrfToken);
    }

    public static HttpResponse sendPostRequest(String url, String cookie, String csrfToken, JSONObject body) {
        return sendRequest(HttpRequest.post(url).body(body.toString()), cookie, csrfToken);
    }

    public static HttpResponse sendPutRequest(String url, String cookie, String csrfToken, JSONObject body) {
        return sendRequest(HttpRequest.put(url).body(body.toString()), cookie, csrfToken);
    }

    public static HttpResponse sendDeleteRequest(String url, String cookie, String csrfToken) {
        return sendRequest(HttpRequest.delete(url), cookie, csrfToken);
    }

    private static HttpResponse sendRequest(HttpRequest request, String cookie, String csrfToken) {
        return request
                .header(COOKIE_HEADER, cookie)
                .header(CSRF_TOKEN_HEADER, csrfToken)
                .execute();
    }
}
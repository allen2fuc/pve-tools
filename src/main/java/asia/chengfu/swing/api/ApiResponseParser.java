package asia.chengfu.swing.api;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ApiResponseParser {

    private static final String STATUS_MESSAGE_HEADER = null;

    public static JSONObject parseResponse(HttpResponse response) {
        if (response.isOk()) {
            return JSONUtil.parseObj(response.body());
        } else {
            // 从Headers中获取Status的错误信息
            Map<String, List<String>> headers = response.headers();
            String statusMessage = Optional.ofNullable(headers.get(STATUS_MESSAGE_HEADER))
                    .flatMap(list -> list.stream().findFirst())
                    .orElse("No status message found");

            log.error("API request failed: {} - {}", statusMessage, response.body());

            throw new RuntimeException(statusMessage);
        }
    }
}
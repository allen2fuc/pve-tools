package asia.chengfu.swing;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ApiResponseParser {

    private static final Logger logger = LoggerFactory.getLogger(ApiResponseParser.class);

    private static final String STATUS_MESSAGE = null;

    public static JSONObject parseResponse(HttpResponse response) {
        if (response.isOk()) {
            return JSONUtil.parseObj(response.body());
        } else {
            // 从Headers中获取Status的错误信息
            Map<String, List<String>> headers = response.headers();
            String statusMessage = Optional.ofNullable(headers.get(STATUS_MESSAGE))
                                          .flatMap(list -> list.stream().findFirst())
                                          .orElse("No status message found");

            logger.error("API request failed: {} - {}", statusMessage, response.body());

            throw new RuntimeException(statusMessage);
        }
    }
}
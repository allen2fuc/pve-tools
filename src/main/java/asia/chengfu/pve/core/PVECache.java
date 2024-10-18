package asia.chengfu.pve.core;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PVECache {
    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    private static final String PVE_FORCE_REFRESH_KEY = "PVE_FORCE_REFRESH";
    private static final String PVE_LOGIN_COOKIE_KEY = "PVE_LOGIN_COOKIE";
    private static final String PVE_LOGIN_TOKEN_KEY = "PVE_LOGIN_TOKEN";

    public static void openRefreshFlag(){
        CACHE.put(PVE_FORCE_REFRESH_KEY, true);
    }

    public static void closeRefreshFlag(){
        CACHE.put(PVE_FORCE_REFRESH_KEY, false);
    }

    public static boolean checkRefreshFlag(){
        Object v = get(PVE_FORCE_REFRESH_KEY);
        if (v != null){
            return (Boolean)v;
        }
        return false;
    }

    public static void setCookie(String ticket){
        set(PVE_LOGIN_COOKIE_KEY, ticket);
    }

    public static String getCookie(){
        return getStr(PVE_LOGIN_COOKIE_KEY);
    }

    public static void setToken(String token){
        set(PVE_LOGIN_TOKEN_KEY, token);
    }

    public static String getToken(){
        return getStr(PVE_LOGIN_TOKEN_KEY);
    }

    private static void set(String key, Object value){
        CACHE.put(key, value);
    }

    private static Object get(String key){
        return CACHE.get(key);
    }

    private static String getStr(String key){
        return MapUtil.getStr(CACHE, key);
    }

    private static Object getOrDefault(String key, Object defaultValue){
        return CACHE.getOrDefault(key, defaultValue);
    }

    static {
        openRefreshFlag();
    }


}

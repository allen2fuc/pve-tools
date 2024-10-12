package asia.chengfu.pve.core;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PVECache {
    private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

    private static final ThreadLocal<String> CURRENT_NODE = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_VMID = new ThreadLocal<>();

    private static final String PVE_FORCE_REFRESH_KEY = "PVE_FORCE_REFRESH";
    private static final String PVE_ADDRESS_KEY = "PVE_ADDRESS";
    private static final String PVE_LOGIN_USER_KEY = "PVE_LOGIN_USER";
    private static final String PVE_LOGIN_PASS_KEY = "PVE_LOGIN_PASS";
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

    public static void setPveHost(String host){
        set(PVE_ADDRESS_KEY, "https://" + host + ":8006");
    }

    public static String getPveAddress(){
        String address = getStr(PVE_ADDRESS_KEY);
        return StrUtil.isNotBlank(address) ? address : "https://127.0.0.1:8006";
    }

    public static void setPveLoginUser(String username){
        set(PVE_LOGIN_USER_KEY, username);
    }

    public static String getPveLoginUser(){
        return getStr(PVE_LOGIN_USER_KEY);
    }

    public static void setPveLoginPass(String password){
        set(PVE_LOGIN_PASS_KEY, password);
    }

    public static String getPveLoginPass(){
        return getStr(PVE_LOGIN_PASS_KEY);
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

    public static void setCurrentNode(String node){
        CURRENT_NODE.set(node);
    }

    public static String getCurrentNode(){
        return CURRENT_NODE.get();
    }

    public static void setCurrentVmid(String vmid){
        CURRENT_VMID.set(vmid);
    }

    public static String getCurrentVmid(){
        return CURRENT_VMID.get();
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

    static {
        openRefreshFlag();
    }
}

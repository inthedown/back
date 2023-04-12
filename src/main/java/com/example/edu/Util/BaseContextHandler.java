package com.example.edu.Util;

import com.example.edu.Entity.User;

import java.util.HashMap;
import java.util.Map;

public class BaseContextHandler {
    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    public static void set(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = threadLocal.get();
        if (map == null) {
            map = new HashMap<>();
            threadLocal.set(map);
        }
        return map.get(key);
    }

    public static String getToken() {
        Object value = get(CommonConstants.CONTEXT_KEY_USER_TOKEN);
        return value == null ? null : value.toString();
    }

    public static void setToken(String token) {
        set(CommonConstants.CONTEXT_KEY_USER_TOKEN, token);
    }

    public static User getUser() {
        return (User) get(CommonConstants.JWT_KEY_USER_INFO);
    }

    public static void setUser(User user){
        BaseContextHandler.set(CommonConstants.JWT_KEY_USER_INFO, user);
    }

    public static void remove() {
        threadLocal.remove();
    }


}

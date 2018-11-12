package com.android.launcher3.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by abylee
 * Date 2015/10/13
 * json解析
 */
public class JsonUtil {

    //json变成类
    public static Object parsData(String json, Class clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    //json变成类(含泛型)
    public static Object parsData(String json, TypeToken type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, type.getType());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
    //json变成类(含泛型)
    public static Object parsData(String json, Type type) {
        try {
            Gson gson = new Gson();
            Object result = gson.fromJson(json, type);
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
    //json对象转字符串
    public static String objectToString(Object o) {
        try {
            Gson gson = new Gson();
            return gson.toJson(o);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    //json变成类（有集合有泛型）
    public static <T> Object parsListData(String json, Class clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}

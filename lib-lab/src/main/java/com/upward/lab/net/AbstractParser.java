package com.upward.lab.net;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Func: 将网络请求响应数据解析成对象Java Bean对象的抽象类
 * Date: 2016-05-12 10:07
 * Author: Will Tang (djtang@iflytek.com)
 * Version: 2.0.12
 */
public abstract class AbstractParser<T> {

    /**
     * 解析JSONObject为Java Bean对象
     *
     * @param json JSONObject字符串
     * @return Java Bean对象
     * @throws Exception
     */
    public abstract T parse(String json) throws Exception;

    /**
     * 解析JSONArray为Java Bean List对象
     *
     * @param json JSONArray字符串
     * @return Java Bean List对象
     * @throws Exception
     */
    public ArrayList<T> parseList(String json) throws Exception {
        ArrayList<T> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.optJSONObject(i);
                if (item != null) list.add(parse(item.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

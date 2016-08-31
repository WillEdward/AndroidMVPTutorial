package com.upward.lab.net;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.upward.lab.exception.UPException;
import com.upward.lab.handler.Dispatch;
import com.upward.lab.util.UPLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Func: 网络请求工具类
 * Date: 2016-05-10 16:26
 * Author: Will Tang (djtang@iflytek.com)
 * Version: 2.0.12
 */
public class RequestManager {

    private static final String TAG = "RequestManager";

    private static final String GET = "GET";
    private static final String POST = "POST";

    private static final int CODE_SUCCESS = 200;

    // 请求方法类型
    private String method;
    // 请求URL
    private String url;
    // 请求参数
    private Map<String, String> params = new HashMap<>();
    // 请求头
    private Map<String, String> headers = new HashMap<>();
    /*
     * 数据解析类
     * 1. 如果不指定具体的解析类，则自动将JSONObject转为对应的Java Bean，将JSONArray转为对应的Java Bean列表
     * 2. 如果指定的化，则根据解析类内部的规则将JSONObject转为指定的Java Bean，将JSONArray转为指定的Java Bean列表
     */
    private AbstractParser parser;

    private RequestManager() {
    }

    public static RequestManager newInstance() {
        return new RequestManager();
    }

    /**
     * 设置请求类型为GET请求
     */
    public RequestManager get() {
        method = GET;
        return this;
    }

    /**
     * 设置请求类型为POST请求
     */
    public RequestManager post() {
        method = POST;
        return this;
    }

    /**
     * 设置请求URL
     */
    public RequestManager url(String url) {
        if (url == null || url.length() == 0 || !url.startsWith("http")) {
            throw new IllegalArgumentException("url is invalid, url must start with http");
        }
        this.url = url;
        return this;
    }

    /**
     * 添加请求参数
     */
    public RequestManager addParams(String name, String value) {
        if (url == null) throw new IllegalStateException("invoke url() method first");
        params.put(name, value);
        return this;
    }

    /**
     * 添加请求参数
     */
    public RequestManager addParams(String name, int value) {
        if (url == null) throw new IllegalStateException("invoke url() method first");
        params.put(name, String.valueOf(value));
        return this;
    }

    /**
     * 设置请求参数
     */
    public RequestManager params(Map<String, String> params) {
        if (url == null) throw new IllegalStateException("invoke url() method first");
        this.params = params;
        return this;
    }

    /**
     * 添加请求头
     */
    public RequestManager addHeaders(String name, String value) {
        if (url == null) throw new IllegalStateException("invoke url() method first");
        headers.put(name, value);
        return this;
    }

    /**
     * 设置请求头
     */
    public RequestManager headers(Map<String, String> headers) {
        if (url == null) throw new IllegalStateException("invoke url() method first");
        this.headers = headers;
        return this;
    }

    /**
     * 设置数据解析类
     */
    public <T extends AbstractParser> RequestManager parser(Class<T> clz) {
        try {
            this.parser = clz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 将请求加入请求队列
     */
    public <T> void enqueue(final UPActionCallback<T> callback) {
        UPLog.d(TAG, "[" + method + " REQUEST]   URL:" + url);
        for (String key : params.keySet()) {
            if (POST.equals(method)) {
                UPLog.d(TAG, "[params] " + key + "=" + params.get(key));
            }
        }
        final Request request = buildRequest();
        if (callback != null) callback.onStart();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                e.printStackTrace();
                if (callback != null) {
                    Dispatch.getInstance().postDelayedByUIThread(new Runnable() {
                        @Override
                        public void run() {
                            UPException ex = new UPException(e.getMessage());
                            UPLog.d(TAG, ex.toString());
                            callback.onFailure(ex);
                            callback.onFinish();
                        }
                    }, 0);
                }
                UPLog.d(TAG, "[REQUEST ERROR]   URL:" + url);
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                try {
                    String data = response.body().string();
                    UPLog.d(TAG, "[REQUEST SUCCESS] URL:" + url + " DATA:" + data);
                    if (callback == null) return;
                    if (response.isSuccessful()) {
                        if (validateData(callback, data)) {
                            data = new JSONObject(data).optString("data");
                            final T javaBean;
                            if (parser != null) {
                                javaBean = handleByParser(callback, data);
                            } else {
                                javaBean = handleByDefault(callback, data);
                            }
                            Dispatch.getInstance().postDelayedByUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onResponse(javaBean);
                                }
                            }, 0);
                        }
                    } else {
                        Dispatch.getInstance().postDelayedByUIThread(new Runnable() {
                            @Override
                            public void run() {
                                UPException e = new UPException("Response is not successful, response code :" + response.code());
                                UPLog.d(TAG, e.toString());
                                callback.onFailure(e);
                            }
                        }, 0);
                    }
                } catch (final Exception e) {
                    if (callback != null) {
                        Dispatch.getInstance().postDelayedByUIThread(new Runnable() {
                            @Override
                            public void run() {
                                UPException ex = new UPException(e.getMessage());
                                UPLog.d(TAG, ex.toString());
                                callback.onFailure(ex);
                            }
                        }, 0);
                    }
                }
                if (callback != null) {
                    Dispatch.getInstance().postDelayedByUIThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFinish();
                        }
                    }, 0);
                }
            }
        });
    }

    // 校验返回数据是否出现异常
    private boolean validateData(final UPActionCallback callback, String data) {
        boolean result = true;
        try {
            final JSONObject jsonObject = new JSONObject(data);
            final int code = jsonObject.optInt("code");
            final String message = jsonObject.optString("data");
            if (code != CODE_SUCCESS) {
                result = false;
                Dispatch.getInstance().postDelayedByUIThread(new Runnable() {
                    @Override
                    public void run() {
                        UPException e = new UPException(code, message);
                        UPLog.d(TAG, e.toString());
                        callback.onFailure(e);
                    }
                }, 0);
            }
        } catch (final JSONException e) {
            e.printStackTrace();
            Dispatch.getInstance().postDelayedByUIThread(new Runnable() {
                @Override
                public void run() {
                    UPException ex = new UPException(e.getMessage());
                    UPLog.d(TAG, ex.toString());
                    callback.onFailure(ex);
                }
            }, 0);
            result = false;
        }
        return result;
    }

    // 指定数据解析类，通过解析类的规则转换为相应的Java Bean
    private <T> T handleByParser(final UPActionCallback<T> callback, String data) throws Exception {
        T t;
        if (callback.isListType()) {
            t = (T) parser.parseList(data);
        } else {
            t = (T) parser.parse(data);
        }
        return t;
    }

    // 未指定数据解析类，使用默认的解析规则
    private <T> T handleByDefault(final UPActionCallback<T> callback, String data) throws Exception {
        return new Gson().fromJson(data, callback.getType());
    }

    // 构建Request对象
    private Request buildRequest() {
        Request.Builder builder = new Request.Builder();
        String paramsStr;
        switch (method) {
            case POST:
                // final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                final MediaType TEXT = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(TEXT, mapToString(params));
                builder.post(body);
                params.clear();
                break;
            default:
                break;
        }
        initHeader();
        for (String header : headers.keySet()) {
            String value = headers.get(header);
            if (value == null) {
                value = "";
            }
            builder.addHeader(header, value);
            // Logging.d(TAG, "[REQUEST Header] " + header + ":" + headers.get(header));
        }

        paramsStr = mapToString(params);

        if (!url.contains("?")) {
            url += "?";
        }
        if (!url.endsWith("?")) {
            url += "&";
        }
        url += paramsStr;
        builder.url(url);
        return builder.build();
    }

    // 添加默认的请求头信息
    private void initHeader() {
    }

    // 将Map转为String，健值之间以=分割，健值对之间以&分割
    private String mapToString(Map<String, String> map) {
        StringBuilder str = new StringBuilder();
        List<String> paramNameList = new ArrayList<>();
        for (String paramName : map.keySet()) {
            paramNameList.add(paramName);
        }
        Collections.sort(paramNameList);
        for (String key : paramNameList) {
            try {
                final String value = URLEncoder.encode(map.get(key), "utf-8");
                str.append(key).append("=").append(value).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (str.toString().endsWith("&")) {
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }

    // 将Map转为JSON字符串
    private String mapToJsonString(Map<String, String> map) {
        return new Gson().toJson(map);
    }

}
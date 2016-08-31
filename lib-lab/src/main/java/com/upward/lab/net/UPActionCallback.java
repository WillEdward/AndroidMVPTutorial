package com.upward.lab.net;

import com.upward.lab.exception.UPException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Func: 操作回调
 * Date: 2016-05-11 08:48
 * Author: Will Tang (upward.edu@gmail.com)
 * Version: 1.0.0
 */
public abstract class UPActionCallback<T> {

    /**
     * 操作开始
     */
    public void onStart() {

    }

    /**
     * 操作返回
     *
     * @param t
     */
    public abstract void onResponse(T t);

    /**
     * 操作异常
     *
     * @param e
     */
    public abstract void onFailure(UPException e);

    /**
     * 操作完成
     */
    public void onFinish() {

    }

    /**
     * 获取范型T的真实类型
     */
    public Type getType() {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        return types[0];
    }

    /**
     * 范型类型是否是List类型
     */
    public boolean isListType() {
        return getType() instanceof ParameterizedType;
    }

}

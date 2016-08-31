package com.upward.lab.exception;

/**
 * Func: 操作异常类.
 * Date: 2016-06-15 23:21
 * Author: Will Tang (upward.edu@gmail.com)
 * Version: 1.0.0
 */
public class UPException {

    // 异常码
    private int code;
    // 异常信息
    private String message;

    public UPException(String message) {
        this.message = message;
    }

    public UPException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UPException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

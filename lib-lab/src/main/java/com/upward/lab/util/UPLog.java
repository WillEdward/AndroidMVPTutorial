package com.upward.lab.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UPLog {

    private static final String TAG = "gugu";

    protected static boolean enabled = true;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        UPLog.enabled = enabled;
    }

    public static int v(String tag, String msg) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.v(tag, msg);
        }
        return result;
    }

    public static int v(String tag, String msg, Throwable tr) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.v(tag, msg, tr);
        }
        return result;
    }

    public static int d(String tag, String msg) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.d(tag, msg);
        }
        return result;
    }

    public static int d(String tag, String msg, Throwable tr) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.d(tag, msg, tr);
        }
        return result;
    }

    public static int i(String tag, String msg) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.i(tag, msg);
        }
        return result;
    }

    public static int i(String tag, String msg, Throwable tr) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.i(tag, msg, tr);
        }
        return result;
    }

    public static int w(String tag, String msg) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.w(tag, msg);
        }
        return result;
    }

    public static int w(String tag, String msg, Throwable tr) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.w(tag, msg, tr);
        }
        return result;
    }

    public static int e(String tag, String msg) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();
            result = Log.e(tag, msg);
        }
        return result;
    }

    public static int e(String tag, String msg, Throwable tr) {
        int result = 0;
        if (enabled) {
            msg += getLogPosition();

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            try {
                tr.printStackTrace(pw);
                msg += "\n" + sw.toString();
            } finally {
                pw.close();
            }

            result = Log.e(tag, msg, tr);
        }
        return result;
    }

    // 获取日志打印的位置，
    private static String getLogPosition() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement ele : elements) {
            if (ele.isNativeMethod()) continue;
            if (ele.getClassName().equals(Thread.class.getName())) continue;
            if (ele.getClassName().equals(UPLog.class.getName())) continue;
            return "[" + ele.getFileName() + ":" + ele.getLineNumber() + " " + ele.getMethodName() + "()]";
        }
        return null;
    }

}

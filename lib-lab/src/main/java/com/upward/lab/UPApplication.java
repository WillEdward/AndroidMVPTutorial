package com.upward.lab;/**
 * Created by tang on 7/4/16.
 */

import android.app.Application;

/**
 * Func: // TODO add class function here.
 * Date: 2016-07-04 16:13
 * Author: Will Tang (upward.edu@gmail.com)
 * Version: 1.0.0
 */
public class UPApplication extends Application {

    private static UPApplication app;

    public UPApplication() {
        app = this;
    }

    public static Application getApp() {
        return app;
    }

}

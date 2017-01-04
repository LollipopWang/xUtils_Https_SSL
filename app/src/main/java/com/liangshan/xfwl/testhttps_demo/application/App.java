package com.liangshan.xfwl.testhttps_demo.application;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2016/12/22.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
    }


}

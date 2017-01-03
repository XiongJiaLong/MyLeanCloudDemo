package com.example.administrator.myleanclouddemo.application;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Administrator on 2017/1/3.
 */

public class MyApplication extends Application {
    private static MyApplication context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        AVOSCloud.initialize(this,"GOSVpCnbBHlS1K0geS15fPNv-gzGzoHsz","UE4tHHNPlpyGcyniXMoj6hYQ");
    }
    public static MyApplication getInstance(){
        return context;
    }
}

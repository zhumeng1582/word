package com.my.words;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.danikula.videocache.HttpProxyCacheServer;
import com.my.words.util.MMKVManager;

public class App extends Application {

    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();
        MMKVManager.init(this);
    }

    public static HttpProxyCacheServer getProxy() {
        App app = (App) Utils.getApp();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheFilesCount(20)
                .build();
    }
}
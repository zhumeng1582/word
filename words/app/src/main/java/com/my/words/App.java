package com.my.words;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.danikula.videocache.HttpProxyCacheServer;

public class App extends Application {

    private HttpProxyCacheServer proxy;

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
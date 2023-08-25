package com.my.words

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.Utils
import com.danikula.videocache.HttpProxyCacheServer
import com.my.words.beans.WordBean
import com.my.words.database.SQLDatabase
import com.my.words.util.MMKVManager.Companion.init
import com.my.words.util.ThreadUtilsEx

class App : Application() {
    private var proxy: HttpProxyCacheServer? = null
    lateinit var db: SQLDatabase

    override fun onCreate() {
        super.onCreate()
        init(this)
        db = Config.loadData(this)
    }

    private fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer.Builder(this)
            .maxCacheFilesCount(20)
            .build()
    }

    companion object {
        fun getDb():SQLDatabase{
            val app = Utils.getApp() as App
            return app.db
        }
        fun getProxy(): HttpProxyCacheServer {
            val app = Utils.getApp() as App
            return if (app.proxy == null) app.newProxy().also { app.proxy = it } else app.proxy!!
        }
    }
}
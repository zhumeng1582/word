package com.my.words.util

import android.app.Application
import com.tencent.mmkv.MMKV

class MMKVManager private constructor() {
    private val mmkv: MMKV = MMKV.defaultMMKV()

    companion object {
        private val instance by lazy { MMKVManager() }

        @JvmStatic
        fun init(application: Application) {
            MMKV.initialize(application)
        }

        private fun mmkv(): MMKV {
            return instance.mmkv
        }

        fun getString(key: String, defValue: String): String {
            return mmkv().getString(key, defValue) ?: ""
        }

        fun getInt(key: String, defValue: Int): Int {
            return mmkv().getInt(key, defValue)
        }

        fun putString(key: String, defValue: String) {
            mmkv().putString(key, defValue)
        }

        fun putInt(key: String, defValue: Int) {
            mmkv().putInt(key, defValue)
        }

        fun putBoolean(key: String, defValue: Boolean) {
            mmkv().putBoolean(key, defValue)
        }

        fun getBoolean(key: String, defValue: Boolean): Boolean {
            return mmkv().getBoolean(key, defValue)
        }

        fun containsKey(key: String): Boolean {
            return mmkv().containsKey(key)
        }
        fun remove(key: String) {
            mmkv().remove(key)
        }
    }
}
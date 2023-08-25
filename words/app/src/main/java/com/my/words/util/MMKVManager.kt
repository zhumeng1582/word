package com.my.words.util

import android.app.Application
import com.tencent.mmkv.MMKV

class MMKVManager private constructor() {
    private val mmkv: MMKV = MMKV.defaultMMKV()

    companion object {
        private var instance: MMKVManager = MMKVManager()

        @JvmStatic
        fun init(application: Application?) {
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
    }
}
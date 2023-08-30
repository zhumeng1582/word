package com.my.words

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ResourceUtils
import com.my.words.beans.WordBean
import com.my.words.database.SQLDatabase
import com.my.words.util.CacheUtil
import com.my.words.util.ThreadUtilsEx
import com.my.words.util.TimerUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Config {
    val classList = listOf(
        "第一级",
        "第二级",
        "第三级",
        "第四级",
        "第五级",
        "第六级",
        "第七级",
        "第八级",
        "第九级",
        "第十级",
        "第十一级",
        "第十二级"
    )

    fun getBookIndex(): Int {
        val bookName = CacheUtil.getWordBookName()
        return classList.indexOf(bookName)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadData(application: Application): SQLDatabase {
        val db = Room.databaseBuilder(application, SQLDatabase::class.java, "WordBean").build()


        GlobalScope.launch {

            var size = db.word().querySize()
            Log.d("loadData", "size = $size")
            if (size == 0L) {
                for (i in 0..11) {
                    val list = getList(i)
                    for (wordBean in list) {
                        wordBean.level = i
                    }
                    db.word().insert(list)
                }
            }
            size = db.word().querySize()
            Log.d("loadData", "size = $size")

        }
        return db
    }

    private fun getList(name: Int): List<WordBean> {
        val json = ResourceUtils.readAssets2String("${name}.json")
        return GsonUtils.fromJson(json, GsonUtils.getListType(WordBean::class.java))
    }

}
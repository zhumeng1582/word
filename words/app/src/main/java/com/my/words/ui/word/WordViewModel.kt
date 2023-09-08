package com.my.words.ui.word

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.my.words.App
import com.my.words.Config
import com.my.words.R
import com.my.words.beans.LearnRecord
import com.my.words.beans.WordBean
import com.my.words.beans.addRecord
import com.my.words.beans.getAudioUrl
import com.my.words.beans.getExample
import com.my.words.audio.PlayAudio
import com.my.words.audio.PlayListener
import com.my.words.util.CacheUtil
import com.my.words.util.TimerUtil
import com.tom_roush.harmony.awt.AWTColor
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.concurrent.TimeUnit


class WordViewModel : ViewModel() {
    var TAG = "WordViewModel"
    val beanList: MutableLiveData<List<WordBean>> = MutableLiveData(arrayListOf())
    val playIcon: MutableLiveData<Int> = MutableLiveData(R.mipmap.icon_play_1)
    private var isPlaying = false
    var type = ""

    init {
        timer()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setListType(type: String) {
        this.type = type
        this@WordViewModel.beanList.postValue(arrayListOf())

        GlobalScope.launch {
            when (type) {
                "LEARNT", "ERROR", "DONE", "ALL" -> {
                    this@WordViewModel.beanList.postValue(getList(type))
                }

                else -> {
                    this@WordViewModel.beanList.postValue(
                        App.getDb().word()
                            .queryAllNotLearn(type.toInt(), CacheUtil.getLearnWordId())
                    )
                }
            }
        }
    }


    //是学习类型还是复习类型
    fun isLearnType(): Boolean {
        return try {
            type.toInt()
            true
        } catch (_: NumberFormatException) {
            false
        }
    }


    fun export(context: Context) {
        ThreadUtils.executeByCached(object : ThreadUtils.Task<String>() {
            override fun doInBackground(): String {
                // 创建PDF文档
                val pdfPath = "${PathUtils.getExternalAppFilesPath()}/word.pdf"
                val document = PDDocument()
                val yahe = getFont(document,R.raw.yahe)
                val lsansuni = getFont(document,R.raw.lsansuni)

                beanList.value?.let {
                    val countPerPage = 6
                    val pageSize = it.size / countPerPage + 1

                    for (i in 0 until pageSize) {
                        val blankPage = PDPage()
                        // 添加内容到页面
                        val contentStream = PDPageContentStream(
                            document,
                            blankPage,
                            PDPageContentStream.AppendMode.PREPEND,
                            false
                        )


                        contentStream.beginText()
                        contentStream.newLineAtOffset(80F, 780F)
                        for (j in 0 until countPerPage) {
                            val index = i * countPerPage + j
                            if (index < it.size) {
                                val item = it[index]
                                contentStream.setFont(lsansuni, 14F)
                                contentStream.setNonStrokingColor(AWTColor.blue)
                                contentStream.showText("${index + 1}. ${item.name} ${item.phonetic}")

                                contentStream.setFont(yahe, 12F)
                                contentStream.newLineAtOffset(0F, -20F)
                                contentStream.setNonStrokingColor(AWTColor.black)
                                contentStream.showText(item.interpret)
                                contentStream.newLineAtOffset(0F, -20F)
                                contentStream.setNonStrokingColor(AWTColor.gray)
                                val lines: List<String> = item.getExample().split("\n")
                                for (line in lines) {
                                    contentStream.showText(line)
                                    contentStream.newLineAtOffset(0F, -20F)
                                }
                                contentStream.newLineAtOffset(0F, -10F)
                            }
                        }
                        contentStream.endText()
                        // 保存文档并关闭
                        contentStream.close()
                        document.addPage(blankPage)
                    }

                    document.save(pdfPath)
                    document.close()
                    Log.d(TAG, "-------pdfPath = " + pdfPath)
                    ToastUtils.showLong("保存成功")
                }

                return pdfPath
            }

            override fun onCancel() {
            }

            override fun onFail(t: Throwable?) {
            }

            override fun onSuccess(result: String?) {

                val file =  FileUtils.getFileByPath(result) // 这里应该是你的 PDF 文件的实际路径
                val uri = FileProvider.getUriForFile(context, "com.my.words.provider", file)
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uri)
                    type = "application/pdf"
                    // 授予临时权限
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
            }

        })
    }

    private fun getFont(document: PDDocument, res: Int): PDType0Font {
        val fontStream: InputStream = Utils.getApp().resources.openRawResource(res)
        return PDType0Font.load(document, fontStream)
    }

    fun getTitle(): String {
        return when (type) {
            "LEARNT" -> "已学习单词:${beanList.value?.size}"
            "ERROR" -> "错误单词:${beanList.value?.size}"
            "DONE" -> "已完成单词:${beanList.value?.size}"
            "ALL" -> "全部单词:${getLearnSize()}/${beanList.value?.size}"
            else -> "学习单词"
        }
    }

    fun getTitleDetail(): String {
        return when (type) {
            "LEARNT" -> "已学习单词"
            "ERROR" -> "错误单词"
            "DONE" -> "已完成单词"
            "ALL" -> "全部单词"
            else -> "学习单词"
        }
    }

    fun getLearnWordId(): Int {
        return CacheUtil.getLearnWordId()
    }

    fun getLearnWordIdIndex(): Int {
        var index = beanList.value?.indexOfFirst { getLearnWordId() == it.id } ?: 0
        if (index < 0) {
            index = 0
        }
        Log.d(TAG, "------->getLearnWordIdIndex = " + index)
        return index
    }

    private fun getLearnSize(): Int {
        return beanList.value?.count { it.id <= CacheUtil.getLearnWordId() } ?: 0
    }

    private fun getList(type: String): List<WordBean> {

        return when (type) {
            "LEARNT" -> {
                App.getDb().record().queryAllLearnDistinct()
            }

            "ERROR" -> {
                App.getDb().word().queryAllError()
            }

            "DONE" -> {
                App.getDb().word().queryAllDone()
            }

            else -> {
                App.getDb().word().query(Config.getBookIndex())
            }
        }
    }

    private fun timer() {
        val task: ThreadUtils.SimpleTask<Int> = object : ThreadUtils.SimpleTask<Int>() {
            override fun doInBackground(): Int {
                return 0
            }

            override fun onSuccess(result: Int) {
                if (isPlaying) {
                    val nextIcon = when (playIcon.value) {
                        R.mipmap.icon_play_1 -> R.mipmap.icon_play_4
                        R.mipmap.icon_play_2 -> R.mipmap.icon_play_1
                        R.mipmap.icon_play_3 -> R.mipmap.icon_play_2
                        R.mipmap.icon_play_4 -> R.mipmap.icon_play_3
                        else -> R.mipmap.icon_play_1
                    }
                    playIcon.postValue(nextIcon)
                }

            }
        }
        ThreadUtils.executeByCachedAtFixRate(task, 800, TimeUnit.MILLISECONDS)
    }

    fun addLearnRecord(index: Int) {
        val id = beanList.value?.get(index)?.id
        id?.let {
            addRecord(LearnRecord(wordId = id, TimerUtil.getTodayMills()))
        }
    }

    fun playAudio(index: Int) {
        beanList.value?.get(index)?.let {
            PlayAudio.playAudio(it.getAudioUrl(), object : PlayListener {
                override fun play() {
                    isPlaying = true
                }

                override fun end() {
                    isPlaying = false
                    playIcon.postValue(R.mipmap.icon_play_1)
                }

            })
        }

    }
}
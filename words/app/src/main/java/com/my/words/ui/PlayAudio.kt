package com.my.words.ui

import android.media.MediaPlayer
import android.util.Log
import java.io.IOException

/**
 * 用来播放在线音频
 */
object PlayAudio {
    private val TAG = "PlayOnlineAudio"
    private var prepared = false
    private var mediaPlayer: MediaPlayer? = null

    fun initPlayer(audioUrl: String) {
        Log.d(TAG, "initPlayer:$audioUrl")
        mediaPlayer = MediaPlayer()
        try {
            Log.d(TAG, "setDataSource")
            mediaPlayer?.setDataSource(audioUrl)
            mediaPlayer?.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d(TAG, "setOnPreparedListener")
        mediaPlayer?.setOnPreparedListener {
            Log.d(TAG, "onPrepared")
            prepared = true
            play()
        }
        mediaPlayer?.setOnCompletionListener {
            Log.d(TAG, "onCompletion: play sound.")
        }
        mediaPlayer?.setOnErrorListener { _, i, i1 ->
            Log.d(TAG, "Play online sound onError: $i, $i1")
            true
        }
    }

    fun play() {
        try {
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "Play error: ", e)
        }
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }
    fun stop(){
        mediaPlayer?.stop()
        prepared = false
        mediaPlayer?.prepareAsync()
    }

    fun release() {
        mediaPlayer?.release()
    }
}
package com.my.words.ui

import android.app.Activity
import android.media.MediaPlayer
import android.util.Log
import com.my.words.App
import java.io.IOException


/**
 * 用来播放在线音频
 */
object PlayAudio {
    private val TAG = "PlayOnlineAudio"
    private var prepared = false
    private var mediaPlayer = MediaPlayer()

    fun play(audioUrl: String,playListener:PlayListener) {
        try {
            mediaPlayer.reset()
            Log.d(TAG, "setDataSource")
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d(TAG, "setOnPreparedListener")
        mediaPlayer.setOnPreparedListener {
            Log.d(TAG, "onPrepared")
            prepared = true
            play()
            playListener.play()
        }
        mediaPlayer.setOnCompletionListener {
            Log.d(TAG, "onCompletion: play sound.")
            playListener.end()
        }
        mediaPlayer.setOnErrorListener { _, i, i1 ->
            playListener.end()
            Log.d(TAG, "Play online sound onError: $i, $i1")
            true
        }
    }

    private fun play() {
        try {
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.e(TAG, "Play error: ", e)
        }
    }

    fun pause(audioUrl: String) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    fun stop(audioUrl: String) {
        mediaPlayer.stop()
        prepared = false
        mediaPlayer.prepareAsync()
    }

    fun release(audioUrl: String) {
        mediaPlayer.release()
    }

    fun release() {
        mediaPlayer.release()
    }
}
interface PlayListener{
    fun play()
    fun end()
}
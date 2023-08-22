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
    private var mediaPlayerMap: HashMap<String, MediaPlayer> = HashMap()

    private fun getMediaPlayer(audioUrl: String): MediaPlayer? {
        if (mediaPlayerMap.contains(audioUrl)) {
            return mediaPlayerMap[audioUrl]!!
        }
        return null
    }

    fun initPlayer(audioUrl: String) {
        Log.d(TAG, "initPlayer:$audioUrl")

        var mediaPlayer = getMediaPlayer(audioUrl)
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            mediaPlayerMap[audioUrl] = mediaPlayer
        } else {
            play(audioUrl)
            return
        }

        try {
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
            play(audioUrl)
        }
        mediaPlayer.setOnCompletionListener {
            Log.d(TAG, "onCompletion: play sound.")
        }
        mediaPlayer.setOnErrorListener { _, i, i1 ->
            Log.d(TAG, "Play online sound onError: $i, $i1")
            true
        }
    }

    fun play(audioUrl: String) {
        try {
            val mediaPlayer = getMediaPlayer(audioUrl)
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "Play error: ", e)
        }
    }

    fun pause(audioUrl: String) {
        val mediaPlayer = getMediaPlayer(audioUrl)
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer.pause()
        }
    }

    fun stop(audioUrl: String) {
        val mediaPlayer = getMediaPlayer(audioUrl)
        mediaPlayer?.stop()
        prepared = false
        mediaPlayer?.prepareAsync()
    }

    fun release(audioUrl: String) {
        val mediaPlayer = getMediaPlayer(audioUrl)
        mediaPlayer?.release()
    }

    fun release() {
        for (mutableEntry in mediaPlayerMap) {
            mutableEntry.value.release()
        }
        mediaPlayerMap.clear()
    }
}
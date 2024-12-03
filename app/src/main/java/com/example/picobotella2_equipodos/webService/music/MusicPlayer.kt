package com.example.picobotella.music

import android.content.Context
import android.media.MediaPlayer
import com.example.picobotella2_equipodos.R

object MusicPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    var isMuted = false

    fun start(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music)
            mediaPlayer?.isLooping = true
        }

        if (!isMuted && !isPlaying) {
            mediaPlayer?.start()
            isPlaying = true
        }
    }

    fun pause() {
        if (isPlaying) {
            mediaPlayer?.pause()
            isPlaying = false
        }
    }

    fun resume() {
        if (!isMuted && !isPlaying) {
            mediaPlayer?.start()
            isPlaying = true
        }
    }

    fun toggleMute() {
        isMuted = !isMuted
        if (isMuted) {
            pause()
        } else {
            resume()
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }
}

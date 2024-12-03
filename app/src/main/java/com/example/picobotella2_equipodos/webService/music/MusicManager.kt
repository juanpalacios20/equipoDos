package com.example.picobotella.music

import android.content.Context
import android.media.MediaPlayer
import com.example.picobotella2_equipodos.R

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isMuted = false

    fun initialize(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music)
            mediaPlayer?.isLooping = true
            val savedVolume = loadVolume(context)
            mediaPlayer?.setVolume(savedVolume, savedVolume)
            mediaPlayer?.start()
        }
    }

    fun saveVolume(context: Context, volume: Float) {
        val sharedPref = context.getSharedPreferences("MusicPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat("volume", volume)
            apply()
        }
    }

    fun loadVolume(context: Context): Float {
        val sharedPref = context.getSharedPreferences("MusicPrefs", Context.MODE_PRIVATE)
        return sharedPref.getFloat("volume", 0.5f)
    }

    fun setVolume(context: Context, volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
        saveVolume(context, volume)
    }


    fun startMusic(context: Context) {
        initialize(context)
        if (!isMuted) {
            mediaPlayer?.start()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun muteMusic() {
        isMuted = true
        mediaPlayer?.pause()
    }

    fun unmuteMusic() {
        isMuted = false
        mediaPlayer?.start()
    }

    fun isMusicMuted(): Boolean = isMuted
}



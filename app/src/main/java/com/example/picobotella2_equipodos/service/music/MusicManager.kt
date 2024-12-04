package com.example.picobotella2_equipodos.service.music

import android.content.Context
import android.media.MediaPlayer
import com.example.picobotella2_equipodos.R

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isMuted = false

    fun initialize(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music).apply {
                isLooping = true
                val savedVolume = loadVolume(context)
                setVolume(savedVolume, savedVolume)
            }
        }

        // Cargar el estado de muteo
        loadMuteState(context)

        if (!isMuted && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }


    fun startMusic(context: Context) {
        initialize(context)
        if (!isMuted && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun muteMusic(context: Context) {
        isMuted = true
        mediaPlayer?.pause()
        saveMuteState(context, isMuted)
    }

    fun unmuteMusic(context: Context) {
        isMuted = false
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
        saveMuteState(context, isMuted)
    }

    fun loadMuteState(context: Context) {
        val sharedPref = context.getSharedPreferences("MusicPrefs", Context.MODE_PRIVATE)
        isMuted = sharedPref.getBoolean("isMuted", false)
    }

    private fun saveMuteState(context: Context, state: Boolean) {
        val sharedPref = context.getSharedPreferences("MusicPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isMuted", state)
            apply()
        }
    }

    fun isMusicMuted(): Boolean = isMuted


    fun setVolume(context: Context, volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
        saveVolume(context, volume)
    }

    private fun saveVolume(context: Context, volume: Float) {
        val sharedPref = context.getSharedPreferences("MusicPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat("volume", volume)
            apply()
        }
    }

    private fun loadVolume(context: Context): Float {
        val sharedPref = context.getSharedPreferences("MusicPrefs", Context.MODE_PRIVATE)
        return sharedPref.getFloat("volume", 0.5f)
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
package com.example.picobotella2_equipodos.view.home

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.HomeBinding

class HomeFragment : Fragment() {

    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackgroundMusic()
        setupBlinkingButton()
        setupCountdownTimer()
        setupToolbarIcons()
    }

    private fun setupBackgroundMusic() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.background_music).apply {
            isLooping = true
            start()
        }
    }

    private fun setupBlinkingButton() {
        val blinkAnimation = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 500
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = AlphaAnimation.INFINITE
        }
        binding.btnPressMe.startAnimation(blinkAnimation)
    }

    private fun setupCountdownTimer() {
        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timerText.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                binding.timerText.text = "0"
            }
        }.start()
    }

    private fun setupToolbarIcons() {
        binding.toolbar.findViewById<ImageButton>(R.id.icon_power).setOnClickListener {
            toggleBackgroundMusic()
        }

        binding.toolbar.findViewById<ImageButton>(R.id.icon_star).setOnClickListener {
            // Acción para la estrella (calificación)
        }

        binding.toolbar.findViewById<ImageButton>(R.id.icon_instructions).setOnClickListener {
            // Acción para las instrucciones
        }

        binding.toolbar.findViewById<ImageButton>(R.id.icon_add_challenges).setOnClickListener {
            // Acción para agregar retos
        }

        binding.toolbar.findViewById<ImageButton>(R.id.icon_share).setOnClickListener {
            // Acción para compartir
        }
    }

    private fun toggleBackgroundMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                it.start()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
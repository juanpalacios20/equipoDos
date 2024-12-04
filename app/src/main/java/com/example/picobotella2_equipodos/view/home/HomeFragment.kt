package com.example.picobotella2_equipodos.view.home

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.HomeBinding
import kotlin.random.Random

class HomeFragment : Fragment() {

    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private var bottleSpinPlayer: MediaPlayer? = null
    private var isSoundOn = true
    private val bottleSpinSounds = arrayOf(
        R.raw.spin_sound
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupBackgroundMusic()
        setupBlinkingButton()
        setupShareButton()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            findViewById<ImageButton>(R.id.icon_power).setOnClickListener {
                toggleBackgroundMusic()
            }
            findViewById<ImageButton>(R.id.icon_share).setOnClickListener {
                shareApp()
            }
        }
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
        binding.btnPressMe.setOnClickListener {
            it.clearAnimation()
            playRandomBottleSpinSound()
        }
    }

    private fun setupShareButton() {
        binding.toolbar.findViewById<ImageButton>(R.id.icon_share).setOnClickListener {
            shareApp()
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

    private fun playRandomBottleSpinSound() {
        val randomSound = bottleSpinSounds[Random.nextInt(bottleSpinSounds.size)]
        bottleSpinPlayer = MediaPlayer.create(requireContext(), randomSound).apply {
            start()
            setOnCompletionListener { release() }
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "¡Prueba esta increíble app! https://play.google.com/store/apps/details?id=com.example.picobotella2")
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir vía"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer?.release()
        mediaPlayer = null
        bottleSpinPlayer?.release()
    }
}


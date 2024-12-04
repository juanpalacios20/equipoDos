package com.example.picobotella2_equipodos.view.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.HomeBinding
import com.example.picobotella.music.MusicManager
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaPlayer: MediaPlayer
    private var isSpinning = false
    private var lastRotation = 0f
    private val spinDuration = 3000L // Duración del giro (3 segundos)
    private val maxRotation = 360f // 360 grados

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar las vistas
        val btnPressMe: ImageButton = binding.btnPressMe
        val bottleIcon: ImageView = binding.bottleIcon
        val timerText: TextView = binding.timerText

        // Inicializar MediaPlayer para el sonido de la botella girando
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.spin_sound)
        mediaPlayer.isLooping = true

        // Configuración del botón de instrucciones
        binding.toolbar.findViewById<ImageButton>(R.id.icon_instructions).setOnClickListener {
            findNavController().navigate(R.id.action_homeMain_to_instructions)
        }

        // Configuración del botón de rating
        binding.toolbar.findViewById<ImageButton>(R.id.icon_star).setOnClickListener {
            findNavController().navigate(R.id.action_homeMain_to_rate)
        }

        // Configuración del botón de agregar retos
        binding.toolbar.findViewById<ImageButton>(R.id.icon_add_challenges).setOnClickListener {
            findNavController().navigate(R.id.action_homeMain_to_challenge)
        }

        // Configuración del botón para hacer girar la botella
        btnPressMe.setOnClickListener {
            if (!isSpinning) {
                startSpinning(bottleIcon, timerText, btnPressMe)
            }
        }
    }

    private fun startSpinning(bottleIcon: ImageView, timerText: TextView, btnPressMe: ImageButton) {
        // Deshabilitar el botón
        btnPressMe.isEnabled = false
        btnPressMe.visibility = View.INVISIBLE

        // Pausar la música de fondo
        // (Solo si tienes un MusicManager similar al del código base)
        MusicManager.pauseMusic()

        // Reproducir sonido de giro
        mediaPlayer.start()

        // Configurar rotación
        val randomRotation = (3600 + Random().nextInt(360)).toFloat()
        bottleIcon.rotation = lastRotation
        bottleIcon.animate().rotation(lastRotation + randomRotation)
            .setDuration(spinDuration)
            .withEndAction {
                lastRotation = (lastRotation + randomRotation) % 360
                stopSpinning(timerText, btnPressMe)
            }
            .start()
    }

    private fun stopSpinning(timerText: TextView, btnPressMe: ImageButton) {
        // Detener sonido
        mediaPlayer.stop()

        // Reanudar música de fondo
        MusicManager.startMusic(requireContext())

        // Iniciar cuenta regresiva
        startCountdown(timerText, btnPressMe)
    }

    private fun startCountdown(timerText: TextView, btnPressMe: ImageButton) {
        timerText.visibility = View.VISIBLE

        object : CountDownTimer(3100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished / 1000).toInt().toString()
            }

            override fun onFinish() {
                timerText.visibility = View.INVISIBLE
                showChallenge()
                btnPressMe.isEnabled = true
                btnPressMe.visibility = View.VISIBLE
            }
        }.start()
    }


    private fun showChallenge() {
        val dialog = ChallengeDialogFragment()
        dialog.show(childFragmentManager, "challengeDialog")

        // Reactivar el botón
        binding.btnPressMe.isEnabled = true
        binding.btnPressMe.visibility = ImageButton.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        // Pausar el sonido
        mediaPlayer.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

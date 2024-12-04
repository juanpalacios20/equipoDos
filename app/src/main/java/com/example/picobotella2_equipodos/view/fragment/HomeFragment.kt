package com.example.picobotella2_equipodos.view.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.HomeBinding
import com.example.picobotella2_equipodos.service.music.MusicManager
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

        // Iniciar la música de fondo tan pronto como se cargue la página
        MusicManager.startMusic(requireContext())

        // Inicializar las vistas
        val btnPressMe: ImageButton = binding.btnPressMe
        val bottleIcon: ImageView = binding.bottleIcon
        val timerText: TextView = binding.timerText

        // Hacer que el botón titile (parpadee)
        startBlinkingButton(btnPressMe)

        // Configuración del botón para hacer girar la botella
        btnPressMe.setOnClickListener {
            if (!isSpinning) {
                stopBlinkingButton(btnPressMe) // Detener la animación al presionar el botón
                startSpinning(bottleIcon, timerText, btnPressMe)
            }
        }
    }

    private fun startBlinkingButton(button: ImageButton) {
        val blinkAnimation = AlphaAnimation(1f, 0f).apply {
            duration = 1000  // Duración de un ciclo de animación
            repeatMode = AlphaAnimation.REVERSE  // Reverso para que parpadee
            repeatCount = AlphaAnimation.INFINITE  // Repetir infinitamente
        }
        button.startAnimation(blinkAnimation)
    }

    private fun stopBlinkingButton(button: ImageButton) {
        button.clearAnimation()  // Detener la animación de parpadeo
    }

    private fun startSpinning(bottleIcon: ImageView, timerText: TextView, btnPressMe: ImageButton) {
        btnPressMe.isEnabled = false  // Deshabilitar el botón, pero no lo hace desaparecer

        // Pausar la música de fondo mientras la botella gira
        MusicManager.pauseMusic()

        // Crear un nuevo MediaPlayer para cada giro y reproducir sonido de giro
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.spin_sound).apply {
            isLooping = false
            start()
        }

        // Configurar rotación aleatoria
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
        // Detener el sonido del giro de la botella
        mediaPlayer.stop()

        // Reanudar la música de fondo
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
                btnPressMe.isEnabled = true  // Reactivar el botón
                btnPressMe.visibility = View.VISIBLE  // Asegurarse de que el botón sea visible

                // Reiniciar la animación después de cerrar el reto
                startBlinkingButton(btnPressMe)
            }
        }.start()
    }

    private fun showChallenge() {
        val dialog = ChallengeDialogFragment()
        dialog.show(childFragmentManager, "challengeDialog")

        // También puedes manejar la lógica para cerrar el diálogo y reiniciar el parpadeo en ese punto.
    }

    override fun onPause() {
        super.onPause()
        // Pausar la música de fondo cuando el fragmento no esté visible
        MusicManager.pauseMusic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

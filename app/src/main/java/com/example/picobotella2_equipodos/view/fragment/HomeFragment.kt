package com.example.picobotella2_equipodos.view.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.HomeBinding
import com.example.picobotella2_equipodos.view.ChallengeActivity
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
        btnPressMe.visibility = ImageButton.INVISIBLE

        // Generar una rotación aleatoria
        val randomRotation = Random().nextFloat() * maxRotation
        val rotateAnimation = RotateAnimation(lastRotation, randomRotation,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = spinDuration
        rotateAnimation.fillAfter = true

        // Iniciar la animación de rotación
        bottleIcon.startAnimation(rotateAnimation)

        // Reproducir el sonido de la botella girando
        mediaPlayer.start()

        // Detener la rotación
        Handler().postDelayed({
            stopSpinning(bottleIcon, timerText, randomRotation, btnPressMe)
        }, spinDuration)
    }

    private fun stopSpinning(bottleIcon: ImageView, timerText: TextView, finalRotation: Float, btnPressMe: ImageButton) {
        // Detener el sonido
        mediaPlayer.pause()

        // Actualizar la última dirección de la botella
        lastRotation = finalRotation

        // Mostrar la cuenta regresiva
        startCountdown(timerText, btnPressMe)
    }

    private fun startCountdown(timerText: TextView, btnPressMe: ImageButton) {
        var timeLeft = 3
        val handler = Handler()

        // Mostrar el contador
        val runnable = object : Runnable {
            override fun run() {
                timerText.text = timeLeft.toString()
                timeLeft--

                if (timeLeft >= 0) {
                    handler.postDelayed(this, 1000)
                } else {
                    showChallenge()
                }
            }
        }

        handler.post(runnable)
    }

    private fun showChallenge() {
        // Crear un Intent para abrir ChallengeActivity
        val intent = Intent(requireContext(), ChallengeActivity::class.java)

        // Si necesitas pasar datos adicionales a la actividad
        intent.putExtra("some_key", "some_value")

        startActivity(intent)

        // Reactivar el botón en HomeFragment
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

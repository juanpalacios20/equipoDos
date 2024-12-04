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
import com.example.picobotella2_equipodos.view.retos.RetoActivity
import java.util.*
import com.example.picobotella2_equipodos.view.fragment.InstructionsFragment


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
            // Reemplazar el fragmento actual con InstructionsFragment usando FragmentManager
            val instructionsFragment = InstructionsFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, instructionsFragment) // 'fragment_container' es el contenedor de tu fragmento en el layout
                .addToBackStack(null) // Añadir a la pila para poder navegar hacia atrás
                .commit()
        }

        binding.toolbar.findViewById<ImageButton>(R.id.icon_star).setOnClickListener {
            // Crear instancia del fragmento RateFragment
            val rateFragment = RateFragment()

            // Iniciar una transacción de fragmentos
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, rateFragment) // Asegúrate de que 'fragment_container' sea el ID del contenedor de fragmentos en tu layout
                .addToBackStack(null) // Agregar a la pila para permitir navegación hacia atrás
                .commit()
        }

        // Configuración del botón de agregar retos
        binding.toolbar.findViewById<ImageButton>(R.id.icon_add_challenges).setOnClickListener {
            // Navegar a la actividad RetoActivity
            val intent = Intent(requireContext(), RetoActivity::class.java)
            startActivity(intent)
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

package com.example.picobotella2_equipodos.view.fragment

import android.content.Intent
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
import com.example.picobotella2_equipodos.auth.LoginFragment
import com.example.picobotella2_equipodos.databinding.HomeBinding
import com.example.picobotella2_equipodos.service.music.MusicManager
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

        // Iniciar la música de fondo tan pronto como se cargue la página
        MusicManager.startMusic(requireContext())

        // Inicializar las vistas
        val btnPressMe: ImageButton = binding.btnPressMe
        val bottleIcon: ImageView = binding.bottleIcon
        val timerText: TextView = binding.timerText

        // Inicializar MediaPlayer para el sonido de la botella girando
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.spin_sound)
        mediaPlayer.isLooping = false

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

        binding.toolbar.findViewById<ImageButton>(R.id.icon_logout).setOnClickListener {
            // Reemplazar el fragmento actual con InstructionsFragment usando FragmentManager
            val loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, loginFragment) // 'fragment_container' es el contenedor de tu fragmento en el layout
                .addToBackStack(null) // Añadir a la pila para poder navegar hacia atrás
                .commit()
        }
    }

    private fun startSpinning(bottleIcon: ImageView, timerText: TextView, btnPressMe: ImageButton) {
        btnPressMe.isEnabled = false
        btnPressMe.visibility = View.INVISIBLE

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
        // Pausar la música de fondo cuando el fragmento no esté visible
        MusicManager.pauseMusic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

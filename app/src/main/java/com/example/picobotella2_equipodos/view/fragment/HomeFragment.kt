package com.example.picobotella2_equipodos.view.fragment

import AuthViewModel
import android.content.Intent
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
import com.example.picobotella2_equipodos.auth.LoginFragment
import com.example.picobotella2_equipodos.databinding.HomeBinding
import com.example.picobotella2_equipodos.view.ChallengeActivity
import com.example.picobotella2_equipodos.service.music.MusicManager
import com.example.picobotella2_equipodos.view.retos.RetoActivity
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
    private lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el botón de audio
        val iconPower = binding.toolbar.findViewById<ImageButton>(R.id.icon_power)
        updateAudioIcon(iconPower)

        // Configuración del clic en el botón de audio
        iconPower.setOnClickListener {
            toggleAudioState(iconPower)
        }

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
        binding.toolbar.findViewById<ImageButton>(R.id.icon_logout).setOnClickListener {
            // Opcional: Actualiza el estado del ViewModel si es necesario
            // Asegúrate de tener acceso al ViewModel o usa un evento global

            // Limpia la pila de retroceso y navega al LoginFragment
            parentFragmentManager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        binding.toolbar.findViewById<ImageButton>(R.id.icon_instructions).setOnClickListener {
            // Reemplazar el fragmento actual con InstructionsFragment usando FragmentManager
            val instructionsFragment = InstructionsFragment.newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, instructionsFragment) // 'fragment_container' es el contenedor de tu fragmento en el layout
                .addToBackStack(null) // Añadir a la pila para poder navegar hacia atrás
                .commit()
        }
        binding.toolbar.findViewById<ImageButton>(R.id.icon_add_challenges).setOnClickListener {
            // Navegar a la actividad RetoActivity
            val intent = Intent(requireContext(), RetoActivity::class.java)
            startActivity(intent)
        }
        binding.toolbar.findViewById<ImageButton>(R.id.icon_star).setOnClickListener {
            val rateFragment = RateFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, rateFragment) // Asegúrate de usar el contenedor correcto.
                .addToBackStack(null)
                .commit()
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

    private fun toggleAudioState(icon: ImageButton) {
        if (MusicManager.isMusicMuted()) {
            MusicManager.unmuteMusic(requireContext())
        } else {
            MusicManager.muteMusic(requireContext())
        }
        updateAudioIcon(icon)
    }

    private fun updateAudioIcon(icon: ImageButton) {
        if (MusicManager.isMusicMuted()) {
            icon.setImageResource(R.drawable.ic_sound_off)
        } else {
            icon.setImageResource(R.drawable.ic_sound_on)
        }
    }

    override fun onResume() {
        super.onResume()
        // Respetar el estado de muteo al reanudar
        if (MusicManager.isMusicMuted()) {
            MusicManager.pauseMusic()
        } else {
            MusicManager.startMusic(requireContext())
        }

        // Actualizar el ícono del botón de sonido
        val iconPower = binding.toolbar.findViewById<ImageButton>(R.id.icon_power)
        updateAudioIcon(iconPower)
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

        // Reanudar la música solo si no está muteada
        if (!MusicManager.isMusicMuted()) {
            MusicManager.startMusic(requireContext())
        }

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
        // Crear un Intent para abrir ChallengeActivity
        val intent = Intent(requireContext(), ChallengeActivity::class.java)
        intent.putExtra("some_key", "some_value")

        startActivity(intent)

        // Reactivar el botón en HomeFragment
        binding.btnPressMe.isEnabled = true
        binding.btnPressMe.visibility = ImageButton.VISIBLE
    }


    override fun onPause() {
        super.onPause()
        if (!MusicManager.isMusicMuted()) {
            MusicManager.pauseMusic()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

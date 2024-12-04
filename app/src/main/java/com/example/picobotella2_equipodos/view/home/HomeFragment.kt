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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.findViewById<ImageButton>(R.id.icon_instructions).setOnClickListener {
            // Usar NavController para navegar a InstructionsFragment
            findNavController().navigate(R.id.action_homeMain_to_instructions)
        }

        binding.toolbar.findViewById<ImageButton>(R.id.icon_star).setOnClickListener {
            // Usar NavController para navegar a RateFragment
            findNavController().navigate(R.id.action_homeMain_to_rate)
        }

        binding.toolbar.findViewById<ImageButton>(R.id.icon_add_challenges).setOnClickListener {
            // Usar NavController para navegar a ChallengeFragment (asegúrate de tener la acción configurada)
            findNavController().navigate(R.id.action_homeMain_to_challenge)
        }
    }
}





package com.example.picobotella2_equipodos.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.HomeBinding

class HomeFragment : Fragment() {

    private var _binding: HomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.findViewById<ImageButton>(R.id.icon_instructions).setOnClickListener {
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





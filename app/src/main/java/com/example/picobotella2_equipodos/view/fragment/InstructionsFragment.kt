package com.example.picobotella2_equipodos.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.databinding.DataBindingUtil
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.InstructionsGameBinding

class InstructionsFragment : Fragment() {

    private lateinit var binding: InstructionsGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usamos DataBinding para inflar el layout
        binding = DataBindingUtil.inflate(inflater, R.layout.instructions_game, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración de la lógica de navegación para el botón "back"
        binding.back.setOnClickListener {
            findNavController().navigateUp() // Navega hacia la pantalla anterior
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = InstructionsFragment()
    }
}

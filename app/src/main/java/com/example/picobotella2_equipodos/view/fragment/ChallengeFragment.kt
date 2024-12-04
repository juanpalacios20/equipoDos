package com.example.picobotella2_equipodos.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.ChallengesBinding
import com.example.picobotella2_equipodos.viewModel.RetoViewModel

class ChallengeFragment : Fragment(R.layout.challenges) {

    private lateinit var viewModel: RetoViewModel
    private var _binding: ChallengesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para el fragmento
        _binding = ChallengesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos el ViewModel
        viewModel = ViewModelProvider(this).get(RetoViewModel::class.java)

        // Llamamos al método para obtener el reto aleatorio y el Pokémon
        viewModel.obtenerRetoAleatorio()
        viewModel.obtenerPokemon(requireContext())

        // Llamamos al método para mostrar el diálogo
        showChallengeDialog()
    }

    private fun showChallengeDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Inflamos el layout del diálogo
        val bindingDialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_challenge, null)
        dialog.setContentView(bindingDialog)

        dialog.setCanceledOnTouchOutside(false)

        // Referenciamos las vistas dentro del diálogo
        val pokemonImageView = bindingDialog.findViewById<ImageView>(R.id.pokemonImage)
        val dialogMessage = bindingDialog.findViewById<TextView>(R.id.challengeText)
        val dialogButton = bindingDialog.findViewById<Button>(R.id.closeButton)

        // Observamos el reto aleatorio desde Firestore
        viewModel.retoAleatorio.observe(viewLifecycleOwner) { reto ->
            // Si reto es nulo, se muestra un mensaje por defecto
            dialogMessage.text = reto?.description ?: "Sin reto disponible"
        }

        // Observamos el Pokémon aleatorio desde la API
        viewModel.pokemon.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(imageUrl.replace("http://", "https://"))
                    .into(pokemonImageView)
            } else {
                pokemonImageView.setImageResource(R.drawable.bottle_icon)  // Imagen por defecto
            }
        }

        // Cerramos el diálogo al presionar el botón
        dialogButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

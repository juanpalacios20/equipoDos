package com.example.picobotella2_equipodos.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.viewModel.RetoViewModel

class ChallengeActivity : AppCompatActivity() {

    private lateinit var viewModel: RetoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Cambia a tu layout principal si tienes otro

        // Inicializamos el ViewModel
        viewModel = ViewModelProvider(this).get(RetoViewModel::class.java)

        // Llamamos al método para obtener el reto aleatorio y el Pokémon
        viewModel.obtenerRetoAleatorio()  // Agregado
        viewModel.obtenerPokemon(this)  // Agregado

        // Llamamos al método para mostrar el diálogo
        showChallengeDialog()
    }


    private fun showChallengeDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Inflamos el layout del diálogo
        val binding = LayoutInflater.from(this).inflate(R.layout.dialog_challenge, null)
        dialog.setContentView(binding)

        dialog.setCanceledOnTouchOutside(false)

        // Referenciamos las vistas
        val pokemonImageView = binding.findViewById<ImageView>(R.id.pokemonImage)
        val dialogMessage = binding.findViewById<TextView>(R.id.challengeText)
        val dialogButton = binding.findViewById<Button>(R.id.closeButton)

        // Observamos el reto aleatorio desde Firestore
        viewModel.retoAleatorio.observe(this) { reto ->
            // Si reto es nulo, se muestra un mensaje por defecto
            dialogMessage.text = reto?.description ?: "Sin reto disponible"
        }

        // Observamos el Pokémon aleatorio desde la API
        viewModel.pokemon.observe(this) { imageUrl ->
            if (imageUrl.isNotEmpty()) {
                Glide.with(this)
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

}
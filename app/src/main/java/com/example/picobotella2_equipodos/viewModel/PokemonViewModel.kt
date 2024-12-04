package com.univalle.equipo5.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.picobotella2_equipodos.data.PokemonDTO
import com.example.picobotella2_equipodos.data.PokemonResponse
import com.example.picobotella2_equipodos.webservice.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor( private val apiService:ApiService) :ViewModel() {


    suspend fun fetchPokemons(): String {
        return withContext(Dispatchers.IO) {
            // Supongamos que tienes un Retrofit service configurado
            try {
                val response = apiService.getPokemons() // Llamada suspendida
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, devuelve la URL de la primera imagen
                    response.body()?.pokemonList?.randomOrNull()?.imagenUrl ?: "No image found"
                } else {
                    Log.e("Error", "Error al obtener datos del Pokémon")
                    "Error en la respuesta"
                }
            } catch (e: Exception) {
                Log.e("Error", "Error de red: ${e.message}")
                "Network error"
            }
        }
    }
}
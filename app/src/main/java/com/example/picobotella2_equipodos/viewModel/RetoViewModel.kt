package com.example.picobotella2_equipodos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picobotella2_equipodos.model.Challenge
import com.example.picobotella2_equipodos.repository.RetosRepository
import com.example.picobotella2_equipodos.webService.RetrofitClient
import kotlinx.coroutines.launch

class RetoViewModel : ViewModel() {

    private val repository = RetosRepository()
    private val _listaRetos = MutableLiveData<List<Challenge>>()
    val listaRetos: LiveData<List<Challenge>> get() = _listaRetos
    private val _retoAleatorio = MutableLiveData<Challenge?>()
    val retoAleatorio: LiveData<Challenge?> get() = _retoAleatorio

    private val _pokemon = MutableLiveData<String>()
    val pokemon: LiveData<String> get() = _pokemon

    init {
        obtenerRetos()
    }
    private fun obtenerRetos() {
        repository.obtenerRetos { retos ->
            _listaRetos.value = retos
        }
    }
    fun obtenerRetoAleatorio() {
        viewModelScope.launch {
            val reto = repository.obtenerRetoAleatorio()
            _retoAleatorio.value = reto
        }
    }
    fun obtenerPokemon() {
        viewModelScope.launch {
            try {
                val pokemonName = RetrofitClient.instance.getPokemonList()
                _pokemon.value = pokemonName.toString()
            } catch (e: Exception) {
                _pokemon.value = "Error"
            }
        }
    }

    fun agregarReto(challenge: Challenge){
        repository.agregarReto(challenge)
    }

    fun editarReto(challenge: Challenge){
        repository.editarReto(challenge)
    }

    fun eliminarReto(challenge: Challenge){
        repository.eliminarReto(challenge)
    }

}
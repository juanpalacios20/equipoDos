package com.example.picobotella.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picobotella2_equipodos.model.Pokedex
import com.example.picobotella2_equipodos.webService.RetrofitClient
import kotlinx.coroutines.launch

class PokemonViewModel: ViewModel() {

    private val _pokedex = MutableLiveData<Pokedex>()
    val pokedex: LiveData<Pokedex> get() = _pokedex

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchPokemonList(){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getPokemonList()
                _pokedex.postValue(response)
            } catch (e: Exception){
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}
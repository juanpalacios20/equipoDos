package com.example.picobotella2_equipodos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picobotella2_equipodos.model.Challenge
import com.example.picobotella2_equipodos.repository.RetosRepository

class RetoViewModel : ViewModel() {

    private val repository = RetosRepository()
    private val _listaRetos = MutableLiveData<List<Challenge>>()
    val listaRetos: LiveData<List<Challenge>> get() = _listaRetos

    init {
        obtenerRetos()
    }
    private fun obtenerRetos() {
        repository.obtenerRetos { retos ->
            _listaRetos.value = retos
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
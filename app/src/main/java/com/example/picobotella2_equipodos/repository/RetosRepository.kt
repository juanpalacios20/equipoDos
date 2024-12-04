package com.example.picobotella2_equipodos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.picobotella2_equipodos.model.Challenge
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.picobotella2_equipodos.webService.PokemonApiService
import com.example.picobotella2_equipodos.webService.RetrofitClient

class RetosRepository {

    private val db = FirebaseFirestore.getInstance()
    private val retosCollection =db.collection("challenge")
    private val apiService = RetrofitClient.instance

    fun obtenerRetos(callback: (List<Challenge>) -> Unit) {
        db.collection("challenge")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val retos = mutableListOf<Challenge>()
                    for (document in snapshot.documents) {
                        val reto = document.toObject(Challenge::class.java)
                        if (reto != null) {
                            retos.add(reto)
                        }
                    }
                    callback(retos)
                }
            }
    }
    suspend fun obtenerRetoAleatorio(): Challenge? {
        return try {
            val snapshot = retosCollection.get().await()
            val retos = snapshot.toObjects(Challenge::class.java)
            if (retos.isNotEmpty()) {
                retos.random()  // Devuelve un reto aleatorio
            } else {
                // Retorna un reto de prueba cuando no hay retos disponibles
                Challenge("Reto de prueba", "Este es un reto de prueba porque no se encontraron retos en la base de datos.")
            }
        } catch (e: Exception) {
            // En caso de error, también puedes devolver un reto de prueba
            Challenge("Error en la consulta", "Hubo un error al obtener los retos. Intenta de nuevo más tarde.")
        }
    }

    suspend fun getPokemon(): String {
        val response = apiService.getPokemonList() // Obtener la lista de Pokémon
        return if (response.pokemon.isNotEmpty()) {
            // Seleccionar un Pokémon aleatorio
            val randomPokemon = response.pokemon.random()
            randomPokemon.img // Retornar la URL de la imagen del Pokémon
        } else {
            "https://www.google.com/imgres?q=pokemon&imgurl=https%3A%2F%2Fwww.pokemon.com%2Fstatic-assets%2Fcontent-assets%2Fcms2%2Fimg%2Fpokedex%2Ffull%2F007.png&imgrefurl=https%3A%2F%2Fwww.pokemon.com%2Fes%2Fpokedex%2Fsquirtle&docid=4JPd7l7-o8fBRM&tbnid=JfTWir-nzTeA7M&vet=12ahUKEwi92sDQy42KAxXJVTABHbxnG7kQM3oECFIQAA..i&w=475&h=475&hcb=2&ved=2ahUKEwi92sDQy42KAxXJVTABHbxnG7kQM3oECFIQAA"
        }
    }

    fun agregarReto(reto: Challenge) {
        val retoRef = db.collection("challenge").document()
        retoRef.set(reto)
    }

    fun editarReto(reto: Challenge){
        val retoRef = db.collection("challenge").document(reto.id)
        retoRef.set(reto)
    }

    fun eliminarReto(reto: Challenge){
        val retoRef = db.collection("challenge").document(reto.id)
        retoRef.delete()
    }

}
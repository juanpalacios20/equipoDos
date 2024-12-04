package com.example.picobotella2_equipodos.repository


import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.example.picobotella2_equipodos.model.Challenge
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.content.Context
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RetosRepository {

    private val db = FirebaseFirestore.getInstance()
    private val retosCollection =db.collection("challenge")


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

    suspend fun getRandomPokemonImage(context: Context): String {
        val url = "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json"
        val requestQueue = Volley.newRequestQueue(context)  // Usar el Contexto pasado

        return suspendCancellableCoroutine { continuation ->
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val pokemonArray = response.getJSONArray("pokemon")
                    if (pokemonArray.length() > 0) {
                        val randomPokemonIndex = (0 until pokemonArray.length()).random()
                        val randomPokemon = pokemonArray.getJSONObject(randomPokemonIndex)
                        val imageUrl = randomPokemon.getString("img")
                        continuation.resume(imageUrl)  // Regresar el resultado
                    } else {
                        continuation.resume("")  // Manejo de error
                    }
                },
                { error ->
                    continuation.resume("")  // En caso de error
                }
            )
            requestQueue.add(jsonObjectRequest)
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
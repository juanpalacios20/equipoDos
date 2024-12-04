package com.example.picobotella2_equipodos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.picobotella2_equipodos.model.Challenge
import com.google.firebase.firestore.FirebaseFirestore

class RetosRepository {

    private val db = FirebaseFirestore.getInstance()

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
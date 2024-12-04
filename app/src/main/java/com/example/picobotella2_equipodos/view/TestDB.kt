package com.example.picobotella2_equipodos.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.picobotella2_equipodos.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class TestDB: AppCompatActivity() {

    // Vistas
    private lateinit var newChallenge: EditText
    private lateinit var addChallenge: Button
    private lateinit var challengeList: ListView

    // Datos
    private lateinit var challenges: MutableList<String>
    private lateinit var challengesAdapter: ArrayAdapter<String>

    // Firestore
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_db)

        // Vistas
        newChallenge = findViewById(R.id.editTextTest)
        addChallenge = findViewById(R.id.buttonTest)
        challengeList = findViewById(R.id.listViewTest)

        // Datos
        challenges = ArrayList()
        challengesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, challenges)
        challengeList.adapter = challengesAdapter

        // Eventos
        addChallenge.setOnClickListener( { addTask() })
        challengeList.setOnItemClickListener { _, _, position, _ -> removeTask(position) }

        // Firestore
        db = Firebase.firestore
        loadChallenges()
    }

    private fun loadChallenges() {
        addChallenge.isEnabled = false
        db.collection("challenge")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val task = document.getString("description")
                    task?.let { challenges.add(it) }
                }
                challengesAdapter.notifyDataSetChanged()
                addChallenge.isEnabled = true
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                addChallenge.isEnabled = true
            }
    }

    private fun addTask() {
        val task = newChallenge.text.toString()
        if (task.isNotBlank()) {
            addChallenge.isEnabled = false
            val pair = hashMapOf("description" to task)
            db.collection("challenge")
                .add(pair)
                .addOnSuccessListener {
                    challenges.add(task)
                    challengesAdapter.notifyDataSetChanged()
                    newChallenge.text.clear()
                    addChallenge.isEnabled = true
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    addChallenge.isEnabled = true
                }
        }
    }

    private fun removeTask(i: Int) {
        addChallenge.isEnabled = false
        val taskToRemove = challenges[i]

        db.collection("challenge")
            .whereEqualTo("description", taskToRemove)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    for (document in result) {
                        db.collection("challenge").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                challenges.removeAt(i)
                                challengesAdapter.notifyDataSetChanged()
                                Toast.makeText(this, "Reto eliminado", Toast.LENGTH_SHORT).show()
                                addChallenge.isEnabled = true
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al eliminar el reto", Toast.LENGTH_LONG).show()
                                addChallenge.isEnabled = true
                            }
                    }
                } else {
                    Toast.makeText(this, "Reto no encontrado en Firestore", Toast.LENGTH_LONG).show()
                    addChallenge.isEnabled = true
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al buscar el reto para eliminar", Toast.LENGTH_LONG).show()
                addChallenge.isEnabled = true
            }
    }

}
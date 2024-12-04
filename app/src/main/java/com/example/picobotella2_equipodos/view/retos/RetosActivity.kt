package com.example.picobotella2_equipodos.view.retos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.model.Challenge
import com.example.picobotella2_equipodos.viewModel.RetoViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RetoActivity : AppCompatActivity() {

    private lateinit var lvRetos: ListView
    private lateinit var retoViewModel: RetoViewModel
    private lateinit var btnAgregarReto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.retos)

        lvRetos = findViewById(R.id.lvRetos)
        btnAgregarReto = findViewById(R.id.btnAgregarReto)

        retoViewModel = ViewModelProvider(this).get(RetoViewModel::class.java)

        retoViewModel.listaRetos.observe(this, { retos ->
            val adapter = RetoAdapter(
                this, retos, onDeleteClick = { reto ->
                    mostrarConfirmacionEliminarReto(reto)
                },
                onEditClick = { reto ->
                    mostrarDialogoEditarReto(reto)
                }
                )
            lvRetos.adapter = adapter
        })

        btnAgregarReto.setOnClickListener {
            mostrarDialogoAgregarReto()
        }
    }

    private fun mostrarDialogoAgregarReto() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.agregar_reto_dialog)

        val etDescripcionReto: EditText = dialog.findViewById(R.id.etDescripcionReto)
        val btnAgregar: Button = dialog.findViewById(R.id.btnAgregar)
        val btnCancelar: Button = dialog.findViewById(R.id.btnCancelar)

        btnAgregar.setOnClickListener {
            val descripcion = etDescripcionReto.text.toString().trim()
            if (descripcion.isNotEmpty()) {
                agregarReto(descripcion)
                dialog.dismiss()
            }
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun mostrarConfirmacionEliminarReto(reto: Challenge) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Reto")
            .setMessage("¿Estás seguro de que deseas eliminar este reto?")
            .setPositiveButton("Eliminar") { _, _ ->
                retoViewModel.eliminarReto(reto)
                lvRetos.deferNotifyDataSetChanged()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    fun mostrarDialogoEditarReto(reto: Challenge) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.editar_reto_dialog, null)

        val etDescripcion: EditText = dialogView.findViewById(R.id.etDescripcion)
        val btnGuardar: Button = dialogView.findViewById(R.id.btnGuardar)

        etDescripcion.setText(reto.description)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnGuardar.setOnClickListener {
            val nuevaDescripcion = etDescripcion.text.toString().trim()
            if (nuevaDescripcion.isNotEmpty()) {
                retoViewModel.editarReto(reto.copy(description = nuevaDescripcion))
                lvRetos.deferNotifyDataSetChanged()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }


    private fun agregarReto(descripcion: String) {
        val nuevoReto = Challenge(description = descripcion)
        retoViewModel.agregarReto(nuevoReto)
    }
}
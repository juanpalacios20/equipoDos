package com.example.picobotella2_equipodos.view.retos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.model.Challenge
import com.example.picobotella2_equipodos.viewModel.RetoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RetoActivity : AppCompatActivity() {

    private lateinit var lvRetos: ListView
    private lateinit var retoViewModel: RetoViewModel
    private lateinit var btnAgregarReto: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.challenges)

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
        dialog.setContentView(R.layout.dialog_add_challenge)

        val etDescripcionReto: EditText = dialog.findViewById(R.id.etDescripcionReto)
        val btnAgregar: Button = dialog.findViewById(R.id.btnAgregar)
        val btnCancelar: Button = dialog.findViewById(R.id.btnCancelar)

        btnAgregar.isEnabled = false
        btnAgregar.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray)

        etDescripcionReto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val hasText = !s.isNullOrBlank()
                btnAgregar.isEnabled = hasText
                btnAgregar.backgroundTintList = if (hasText) {
                    ContextCompat.getColorStateList(this@RetoActivity, R.color.orange)
                } else {
                    ContextCompat.getColorStateList(this@RetoActivity, R.color.gray)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnAgregar.setOnClickListener {
            val descripcion = etDescripcionReto.text.toString().trim()
            if (descripcion.isNotEmpty()) {
                agregarReto(descripcion)
                Toast.makeText(this, "Reto guardado", Toast.LENGTH_SHORT).show()
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
            .setTitle("¿Desea eliminar el siguiente reto?")
            .setMessage(reto.description)
            .setPositiveButton("Si") { _, _ ->
                retoViewModel.eliminarReto(reto)
                lvRetos.deferNotifyDataSetChanged()
            }
            .setNegativeButton("No", null)
            .show()
    }

    fun mostrarDialogoEditarReto(reto: Challenge) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_challenge, null)

        val etDescripcion: EditText = dialogView.findViewById(R.id.etDescripcion)
        val btnGuardar: Button = dialogView.findViewById(R.id.btnGuardar)

        etDescripcion.setText(reto.description)

        btnGuardar.isEnabled = false
        btnGuardar.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray)

        // Text watcher to enable/disable the save button
        etDescripcion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val hasText = !s.isNullOrBlank()
                btnGuardar.isEnabled = hasText
                btnGuardar.backgroundTintList = if (hasText) {
                    ContextCompat.getColorStateList(this@RetoActivity, R.color.orange)
                } else {
                    ContextCompat.getColorStateList(this@RetoActivity, R.color.gray)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnGuardar.setOnClickListener {
            val nuevaDescripcion = etDescripcion.text.toString().trim()
            if (nuevaDescripcion.isNotEmpty()) {
                retoViewModel.editarReto(reto.copy(description = nuevaDescripcion))
                lvRetos.deferNotifyDataSetChanged()
                Toast.makeText(this, "Reto actualizado", Toast.LENGTH_SHORT).show()
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
package com.example.picobotella2_equipodos.view.retos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
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
import org.w3c.dom.Text

class RetoActivity : AppCompatActivity() {
    private lateinit var botonBack: ImageButton
    private lateinit var lvRetos: ListView
    private lateinit var retoViewModel: RetoViewModel
    private lateinit var btnAgregarReto: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.challenges)

        lvRetos = findViewById(R.id.lvRetos)
        btnAgregarReto = findViewById(R.id.btnAgregarReto)
        botonBack = findViewById(R.id.back)

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

        botonBack.setOnClickListener() {
            finish()
        }

        btnAgregarReto.setOnClickListener {
            mostrarDialogoAgregarReto()
        }
    }


    private fun mostrarDialogoAgregarReto() {
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_add_challenge, null)

        val etDescripcionReto: EditText = dialog.findViewById(R.id.etDescripcionReto)
        val btnAgregar: Button = dialog.findViewById(R.id.btnAgregar)
        val btnCancelar: Button = dialog.findViewById(R.id.btnCancelar)

        btnAgregar.isEnabled = false
        btnAgregar.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gray)

        val dialogView = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .create()

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
                dialogView.dismiss()
            }
        }

        btnCancelar.setOnClickListener {
            dialogView.dismiss()
        }

        dialogView.show()
    }

    fun mostrarConfirmacionEliminarReto(reto: Challenge) {
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_delete_challenge, null)

        val descripción: TextView = dialog.findViewById(R.id.challengeDescriptionTextView)
        val btnCerrar: TextView = dialog.findViewById(R.id.noButton)
        val btnConfirmar: TextView = dialog.findViewById(R.id.yesButton)

        descripción.text = reto.description

        val dialogView = AlertDialog.Builder(this)
            .setView(dialog)
            .setCancelable(false)
            .create()

        btnConfirmar.setOnClickListener {
            retoViewModel.eliminarReto(reto)
            lvRetos.deferNotifyDataSetChanged()
            Toast.makeText(this, "Reto eliminado", Toast.LENGTH_SHORT).show()
            dialogView.dismiss()
        }

        btnCerrar.setOnClickListener {
            dialogView.dismiss()

        }

        dialogView.show()
    }

    fun mostrarDialogoEditarReto(reto: Challenge) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_challenge, null)

        val etDescripcion: EditText = dialogView.findViewById(R.id.etDescripcion)
        val btnGuardar: Button = dialogView.findViewById(R.id.btnGuardar)
        val btnCancelar: Button = dialogView.findViewById(R.id.btnCancelar)

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
            .setCancelable(false)
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

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun agregarReto(descripcion: String) {
        val nuevoReto = Challenge(description = descripcion)
        retoViewModel.agregarReto(nuevoReto)
    }

}
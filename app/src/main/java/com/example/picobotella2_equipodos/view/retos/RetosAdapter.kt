package com.example.picobotella2_equipodos.view.retos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.model.Challenge

class RetoAdapter(
    context: Context,
    private val retos: List<Challenge>,
    private val onEditClick: (Challenge) -> Unit,
    private val onDeleteClick: (Challenge) -> Unit
) : ArrayAdapter<Challenge>(context, R.layout.item_reto, retos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_reto, parent, false)

        val reto = getItem(position)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditar)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminar)

        // Asignamos los valores a las vistas
        tvDescripcion.text = reto?.description

        btnEditar.setOnClickListener {
            reto?.let {
                onEditClick(it) // Llamamos al callback para editar
            }
        }

        btnEliminar.setOnClickListener {
            reto?.let {
                onDeleteClick(it) // Llamamos al callback para eliminar
            }
        }

        return view
    }
}


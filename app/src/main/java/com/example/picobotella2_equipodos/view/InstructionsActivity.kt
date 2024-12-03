package com.example.picobotella2_equipodos.view

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.picobotella2_equipodos.R


class InstructionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.instructions_game)


        // Configuración del botón de regreso
        val backButton = findViewById<ImageButton>(R.id.back)
        backButton.setOnClickListener {
            finish() // Regresa a la actividad anterior (HomeActivity si es el caso)
        }
    }
}

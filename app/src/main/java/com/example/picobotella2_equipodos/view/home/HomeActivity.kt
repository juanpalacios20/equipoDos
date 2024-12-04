package com.example.picobotella2_equipodos.view.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.databinding.HomeBinding
import com.example.picobotella2_equipodos.view.fragment.RateFragment

class HomeActivity : AppCompatActivity(), RateFragment.OnRateButtonClickedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        // Fragment transaction
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        val reviewButton = findViewById<ImageButton>(R.id.icon_star)
        reviewButton.setOnClickListener {
            val url = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_CO&pli=1"
            val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
            startActivity(intent)
        }
        val shareButton = findViewById<ImageButton>(R.id.icon_share)
        shareButton.setOnClickListener {
            val enlaceApp = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_CO&pli=1"
            val intentCompartir = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "App pico botella")
                putExtra(Intent.EXTRA_TEXT, "Solo los valientes lo juegan!! $enlaceApp")
            }
            startActivity(Intent.createChooser(intentCompartir, "Compartir usando"))
        }
    }



    override fun onRateButtonClicked() {
        // Aquí gestionamos lo que debe hacer la actividad cuando se presione el botón de calificación
        Toast.makeText(this, "Calificación presionada", Toast.LENGTH_SHORT).show()
    }
}


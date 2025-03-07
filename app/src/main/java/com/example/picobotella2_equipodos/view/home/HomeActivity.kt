package com.example.picobotella2_equipodos.view.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.view.fragment.HomeFragment
import com.example.picobotella2_equipodos.view.fragment.RateFragment

class HomeActivity : AppCompatActivity() {

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

}


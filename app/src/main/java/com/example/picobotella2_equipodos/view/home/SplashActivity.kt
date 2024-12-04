package com.example.picobotella2_equipodos.view.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.picobotella2_equipodos.R

class SplashActivity : AppCompatActivity() {
    private val splashTimeOut: Long = 5000 // 5 segundos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val bottleIcon: ImageView = findViewById(R.id.bottleIcon)

        // Carga la animación de rotación
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.animated_bottle)

        // Inicia la animación
        bottleIcon.startAnimation(rotateAnimation)

        // Desactivar la toolbar
        supportActionBar?.hide()

        // Esperar 5 segundos antes de cambiar a la MainActivity
        Handler().postDelayed({
            // Iniciar la actividad principal después de 5 segundos
            val intent = Intent(this, HomeFragment::class.java) // Cambia InicioActivity por el nombre de tu actividad principal
            startActivity(intent)
            finish() // Cierra la SplashActivity para que no vuelva al presionar atrás
        }, splashTimeOut)
    }
}


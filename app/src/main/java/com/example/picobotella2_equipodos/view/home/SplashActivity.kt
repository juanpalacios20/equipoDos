package com.example.picobotella2_equipodos.view.home

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.picobotella2_equipodos.R
import com.example.picobotella2_equipodos.auth.LoginFragment


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

        // Esperar 5 segundos antes de cambiar a LoginFragment
        Handler().postDelayed({
            // Cargar LoginFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment()) // Asegúrate de tener un contenedor en el layout de la actividad
                .commit()

        }, splashTimeOut)
    }
}


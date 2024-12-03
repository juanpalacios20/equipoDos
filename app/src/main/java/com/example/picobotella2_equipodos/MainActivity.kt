package com.example.picobotella2_equipodos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils
import android.widget.ImageView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val bottleIcon: ImageView = findViewById(R.id.bottleIcon)

        // Carga la animación de rotación
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.animated_bottle)

        // Inicia la animación
        bottleIcon.startAnimation(rotateAnimation)
    }
}


package com.example.picobotella2_equipodos

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.picobotella2_equipodos.auth.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val bottleIcon: ImageView = findViewById(R.id.bottleIcon)

        // Carga la animación
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.animated_bottle)

        // Listener para ejecutar algo al terminar la animación
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Muestra el LoginFragment cuando termine el Splash Screen
                showFragment(LoginFragment())
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // Inicia la animación
        bottleIcon.startAnimation(rotateAnimation)
    }

    // Método para cargar fragmentos en el FrameLayout
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}

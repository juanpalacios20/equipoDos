package com.example.picobotella2_equipodos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.picobotella2_equipodos.view.home.HomeFragment
import com.example.picobotella2_equipodos.view.fragment.InstructionsFragment
import android.view.animation.AnimationUtils
import android.widget.ImageView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Comprueba si no hay fragmentos cargados (por ejemplo, tras una rotación)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}


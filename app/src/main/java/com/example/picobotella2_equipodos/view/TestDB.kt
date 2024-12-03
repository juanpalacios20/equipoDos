package com.example.picobotella2_equipodos.view

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.picobotella2_equipodos.R

class TestDB: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.test_db)

        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<TextView>(R.id.button)

        button.setOnClickListener {
            textView.text = "Haz clicado el botón"
        }
    }
}
package com.example.gradhunt.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.gradhunt.MainActivity
import com.example.gradhunt.R

class PasswordRecover : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recover)


        //Boton Go back
        val iniciarSesionTextView: TextView = findViewById(R.id.regresarButton)
        iniciarSesionTextView.setOnClickListener {
            finish()
        }

    }
}
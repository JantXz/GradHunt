package com.example.gradhunt

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.example.gradhunt.activities.Register
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.example.gradhunt.activities.MainNavBar
import com.example.gradhunt.activities.PasswordRecover
import com.example.gradhunt.fragments.Home
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var emailErrorTextView: TextView
    private lateinit var passwordErrorTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var passwordIcon: ImageView
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        emailErrorTextView = findViewById(R.id.emailErrorTextView)
        passwordErrorTextView = findViewById(R.id.passwordErrorTextView)
        loginButton = findViewById(R.id.loginButton)
        passwordIcon = findViewById(R.id.passwordIcon)

        //Register button
        val registerTextView: TextView = findViewById(R.id.registerTextView)
        registerTextView.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        //Boton Log In
        val iniciarSesionTextView: TextView = findViewById(R.id.recoverPassword)
        iniciarSesionTextView.setOnClickListener {
            val intent = Intent(this, PasswordRecover::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            validateFields()
        }

        passwordIcon.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun validateFields() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        var isValid = true

        if (email.isEmpty()) {
            emailErrorTextView.visibility = View.VISIBLE
            emailErrorTextView.text = "Email is required"
            isValid = false
        } else if (!isValidEmail(email)) {
            emailErrorTextView.visibility = View.VISIBLE
            emailErrorTextView.text = "Invalid email address"
            isValid = false
        } else {
            emailErrorTextView.visibility = View.GONE
        }

        if (password.isEmpty()) {
            passwordErrorTextView.visibility = View.VISIBLE
            passwordErrorTextView.text = "Password is required"
            isValid = false
        } else {
            passwordErrorTextView.visibility = View.GONE
        }

        if (isValid) {
            loginUser(email, password)
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordIcon.setImageResource(R.drawable.ic_visibility_off)
        } else {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passwordIcon.setImageResource(R.drawable.ic_visibility)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length) // Move the cursor to the end of the text
    }

    private fun loginUser(email: String, password: String) {
        val url = "http://10.0.2.2:8000/api/v1/login"

        val params = JSONObject()
        params.put("email", email)
        params.put("password", password)
        params.put("device_name", "MyDevice")

        val request = JsonObjectRequest(Request.Method.POST, url, params,
            { response ->
                try {
                    val plainTextToken = response.getString("plain-text-token")
                    // Aquí puedes guardar el token en SharedPreferences o en otra ubicación según tus necesidades
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, MainNavBar::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: JSONException) {
                    Log.e("MainActivity", "Error parsing JSON: ${e.message}", e)
                    Toast.makeText(this, "Login failed: Invalid response", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("MainActivity", "Error: ${error.message}", error)
                Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(this).add(request)
    }

}

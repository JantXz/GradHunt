package com.example.gradhunt.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gradhunt.R
import org.json.JSONObject
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.gradhunt.MainActivity
import com.example.gradhunt.singleton.MySingleton

class Register : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfirmationEditText: EditText
    private lateinit var nameErrorTextView: TextView
    private lateinit var emailErrorTextView: TextView
    private lateinit var passwordErrorTextView: TextView
    private lateinit var passwordConfirmationErrorTextView: TextView
    private lateinit var registerButton: Button
    private lateinit var passwordIcon: ImageView
    private lateinit var passwordConfirmIcon: ImageView
    private var isPasswordVisible: Boolean = false
    private var isPasswordConfirmationVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        passwordConfirmationEditText = findViewById(R.id.passwordConfirmationEditText)
        nameErrorTextView = findViewById(R.id.nameErrorTextView)
        emailErrorTextView = findViewById(R.id.emailErrorTextView)
        passwordErrorTextView = findViewById(R.id.passwordErrorTextView)
        passwordConfirmationErrorTextView = findViewById(R.id.passwordConfirmationErrorTextView)
        registerButton = findViewById(R.id.registerButton)
        passwordIcon = findViewById(R.id.passwordIcon)
        passwordConfirmIcon = findViewById(R.id.passwordConfirmIcon)

        //Boton Log In
        val iniciarSesionTextView: TextView = findViewById(R.id.iniciarSesionButton)
        iniciarSesionTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerButton.setOnClickListener {
            validateFields()
        }

        passwordIcon.setOnClickListener {
            togglePasswordVisibility()
        }

        passwordConfirmIcon.setOnClickListener {
            togglePasswordConfirmationVisibility()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateFields() {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val passwordConfirmation = passwordConfirmationEditText.text.toString().trim()
        val deviceName = "MyDevice" // O recoge el nombre del dispositivo de forma dinámica si lo deseas

        var isValid = true

        if (name.isEmpty()) {
            nameErrorTextView.visibility = View.VISIBLE
            nameErrorTextView.text = "Organization name is required"
            isValid = false
        } else {
            nameErrorTextView.visibility = View.GONE
        }

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

        if (passwordConfirmation.isEmpty()) {
            passwordConfirmationErrorTextView.visibility = View.VISIBLE
            passwordConfirmationErrorTextView.text = "Password confirmation is required"
            isValid = false
        } else if (password != passwordConfirmation) {
            passwordConfirmationErrorTextView.visibility = View.VISIBLE
            passwordConfirmationErrorTextView.text = "Passwords do not match"
            isValid = false
        } else {
            passwordConfirmationErrorTextView.visibility = View.GONE
        }

        if (isValid) {
            registerUser(name, email, password, deviceName)
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

    private fun togglePasswordConfirmationVisibility() {
        if (isPasswordConfirmationVisible) {
            passwordConfirmationEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordConfirmIcon.setImageResource(R.drawable.ic_visibility_off)
        } else {
            passwordConfirmationEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passwordConfirmIcon.setImageResource(R.drawable.ic_visibility)
        }
        isPasswordConfirmationVisible = !isPasswordConfirmationVisible
        passwordConfirmationEditText.setSelection(passwordConfirmationEditText.text.length) // Move the cursor to the end of the text
    }

    private fun registerUser(name: String, email: String, password: String, deviceName: String) {
        val url = "http://10.0.2.2:8000/api/v1/register" // Cambia la URL según tu configuración local

        val params = HashMap<String, String>()
        params["name"] = name
        params["email"] = email
        params["password"] = password
        params["password_confirmation"] = password
        params["device_name"] = deviceName

        val jsonObject = JSONObject(params as Map<*, *>)

        Log.d("RegisterActivity", "Request JSON: $jsonObject")

        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            Log.d("RegisterActivity", "Response: $response")
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, Response.ErrorListener { error ->
            Log.e("RegisterActivity", "Error: ${error.message}", error)
            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            // Maneja el error aquí
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonObject.toString().toByteArray()
            }
        }

        Log.d("RegisterActivity", "Sending request to $url")

        MySingleton.getInstance(this).addToRequestQueue(request)
    }}


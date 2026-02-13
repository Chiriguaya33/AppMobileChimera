package com.matrix.appmobilechimera.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.matrix.appmobilechimera.ui.dashboard.DashboardActivity
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.data.api.LoginRequest
import com.matrix.appmobilechimera.data.api.RetrofitClient
import com.matrix.appmobilechimera.model.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configuración de Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 1. VINCULAR VISTAS DE CREDENCIALES
        val etUser = findViewById<TextInputEditText>(R.id.etUser)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnGoogle = findViewById<MaterialButton>(R.id.btnGoogle)

        // 2. LISTENERS
        btnGoogle.setOnClickListener { signInWithGoogle() }

        btnLogin.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                loginConCredenciales(user, pass)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        verificarSesionExistente()
    }

    // --- LÓGICA DE CREDENCIALES ---
    private fun loginConCredenciales(user: String, pass: String) {
        Toast.makeText(this, "Autenticando...", Toast.LENGTH_SHORT).show()

        // Enviamos username y password al backend
        val request = LoginRequest(username = user, password = pass)

        RetrofitClient.instance.login(request).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful && response.body() != null) {
                    guardarSesion(response.body()!!)
                    irAlDashboard()
                } else {
                    Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // --- LÓGICA DE GOOGLE ---
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcherGoogle.launch(signInIntent)
    }

    private val launcherGoogle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account.email?.let { loginConBackend(it) }
        } catch (e: ApiException) {
            Log.e("CHIMERA_DEBUG", "Error API Google: ${e.statusCode}")
        }
    }

    private fun loginConBackend(email: String) {
        val request = LoginRequest(email = email)
        RetrofitClient.instance.login(request).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful && response.body() != null) {
                    guardarSesion(response.body()!!)
                    irAlDashboard()
                } else {
                    Toast.makeText(this@LoginActivity, "Usuario no matriculado", Toast.LENGTH_LONG).show()
                    googleSignInClient.signOut()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // --- FUNCIONES COMUNES ---
    private fun guardarSesion(user: User) {
        val sharedPref = getSharedPreferences("ChimeraSession", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("USER_ID", user.id)
            putString("FULLNAME", user.fullname)
            putString("EMAIL", user.email)
            putString("TOKEN", user.token)
            apply()
        }
    }

    private fun verificarSesionExistente() {
        val sharedPref = getSharedPreferences("ChimeraSession", Context.MODE_PRIVATE)
        if (sharedPref.getInt("USER_ID", -1) != -1) {
            irAlDashboard()
        }
    }

    private fun irAlDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
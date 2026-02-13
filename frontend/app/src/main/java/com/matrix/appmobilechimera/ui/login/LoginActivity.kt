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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val btnGoogle = findViewById<MaterialButton>(R.id.btnGoogle)
        btnGoogle.setOnClickListener {
            signIn()
        }

        verificarSesionExistente()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        launcherGoogle.launch(signInIntent)
    }

    private val launcherGoogle = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        } else {
            Log.e("CHIMERA_DEBUG", "Google Sign-In cancelado. Code: ${result.resultCode}")
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
        // Asincronía: La petición corre en un hilo secundario para no congelar la UI
        RetrofitClient.instance.login(LoginRequest(email)).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        Log.d("CHIMERA_DEBUG", "Usuario recibido: ID=${user.id}, Nombre=${user.fullname}")
                        guardarSesion(user)
                        irAlDashboard()
                    } else {
                        Log.e("CHIMERA_DEBUG", "Cuerpo de respuesta nulo")
                    }
                } else {
                    // Manejo de errores: Usuario no encontrado en Moodle (404)
                    Toast.makeText(this@LoginActivity, "Usuario no matriculado en Moodle", Toast.LENGTH_LONG).show()
                    cerrarSesionGoogle()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Error de red o Backend apagado
                Log.e("CHIMERA_DEBUG", "Fallo conexión backend: ${t.message}")
                Toast.makeText(this@LoginActivity, "Error de red: ¿Está encendido Python?", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun guardarSesion(user: User) {
        // Persistencia de datos: Guardamos el ID como INT para que los Fragments lo lean bien
        val sharedPref = getSharedPreferences("ChimeraSession", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("USER_ID", user.id) // Clave vital para Home y Missions
            putString("FULLNAME", user.fullname)
            putString("EMAIL", user.email)
            putString("TOKEN", user.token)
            apply() // apply() es asíncrono y más eficiente que commit()
        }
        Log.d("CHIMERA_DEBUG", "Sesión guardada localmente para el ID: ${user.id}")
    }

    private fun verificarSesionExistente() {
        val sharedPref = getSharedPreferences("ChimeraSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)
        if (userId != -1) {
            irAlDashboard()
        }
    }

    private fun cerrarSesionGoogle() {
        googleSignInClient.signOut()
    }

    private fun irAlDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
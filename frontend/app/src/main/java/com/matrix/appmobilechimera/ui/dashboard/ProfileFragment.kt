package com.matrix.appmobilechimera.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // 1. Obtener datos de la sesión (SharedPreferences)
        val sharedPref = requireContext().getSharedPreferences("ChimeraSession", Context.MODE_PRIVATE)
        val fullname = sharedPref.getString("FULLNAME", "Usuario")
        val email = sharedPref.getString("EMAIL", "")

        // 2. Vincular vistas
        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        tvName.text = fullname

        // 3. Lógica de Cerrar Sesión
        btnLogout.setOnClickListener {
            // Borramos los datos guardados
            sharedPref.edit().clear().apply()

            // Navegamos al Login y cerramos el Dashboard
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
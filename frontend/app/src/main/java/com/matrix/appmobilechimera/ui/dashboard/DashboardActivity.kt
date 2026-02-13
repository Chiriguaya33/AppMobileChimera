package com.matrix.appmobilechimera.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.matrix.appmobilechimera.R

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Al iniciar, cargamos el HomeFragment por defecto
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // Listener para los clics en la barra inferior (Callback)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_missions -> replaceFragment(MissionsFragment()) // Debes crear este fragmento vacío
                R.id.nav_profile -> replaceFragment(ProfileFragment())   // Debes crear este fragmento vacío
            }
            true
        }
    }

    // Método genérico para cambiar fragmentos (Modularidad)
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
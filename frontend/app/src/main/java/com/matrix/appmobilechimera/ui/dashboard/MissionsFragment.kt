package com.matrix.appmobilechimera.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.data.api.RetrofitClient
import com.matrix.appmobilechimera.model.Mission
import com.matrix.appmobilechimera.ui.adapter.MissionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MissionsFragment : Fragment() {

    private lateinit var rvMissions: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_missions, container, false)

        // 1. Inicializar Vistas
        rvMissions = view.findViewById(R.id.rvMissions)
        progressBar = view.findViewById(R.id.progressBarMissions) // Asegúrate de tener este ID en tu XML

        rvMissions.layoutManager = LinearLayoutManager(requireContext())

        // 2. Obtener USER_ID de SharedPreferences
        val sharedPref = requireContext().getSharedPreferences("ChimeraSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)

        // 3. Cargar datos reales
        cargarMisiones(userId)

        return view
    }

    private fun cargarMisiones(userId: Int) {
        if (userId == -1) return

        progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getMisiones(userId).enqueue(object : Callback<List<Mission>> {
            override fun onResponse(call: Call<List<Mission>>, response: Response<List<Mission>>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val misiones = response.body() ?: emptyList()

                    // Configuramos el adapter con los datos que vienen de Python/Moodle
                    rvMissions.adapter = MissionAdapter(misiones) { mission ->
                        abrirEnvioDeTarea(mission)
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al obtener misiones", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Mission>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun abrirEnvioDeTarea(mission: Mission) {
        // Punto 4 del Alcance: Envío de tareas (Simulado por ahora)
        Toast.makeText(requireContext(), "Abriendo: ${mission.title}", Toast.LENGTH_SHORT).show()
    }
}
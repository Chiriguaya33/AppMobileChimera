package com.matrix.appmobilechimera.ui.dashboard

import android.os.Bundle
import android.view.View
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
import android.content.Intent


class MissionsFragment : Fragment(R.layout.fragment_missions) {

    private lateinit var rvMissions: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        rvMissions = view.findViewById(R.id.rvMissions)
        rvMissions.layoutManager = LinearLayoutManager(requireContext())

        // Pulido: Cada vez que el fragmento se crea, pedimos los datos frescos
        cargarMisiones()
    }

    private fun cargarMisiones() {
        val userId = requireContext().getSharedPreferences("ChimeraSession", 0).getInt("USER_ID", -1)

        RetrofitClient.instance.getMisiones(userId).enqueue(object : Callback<List<Mission>> {
            override fun onResponse(call: Call<List<Mission>>, response: Response<List<Mission>>) {
                if (response.isSuccessful) {
                    val misiones = response.body() ?: emptyList()

                    // Si la lista está vacía, mostramos un mensaje (C3: Usabilidad)
                    if (misiones.isEmpty()) {
                        // Aquí podrías mostrar un TextView de "No tienes misiones"
                    }

                    rvMissions.adapter = MissionAdapter(misiones) { mission ->
                        // Si ya está entregada, podrías avisar que ya no necesita envío
                        if (mission.status != "Entregado") {
                            abrirEnvioDeTarea(mission)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Mission>>, t: Throwable) {
                // Manejo de errores para que la app no se vea "muerta"
            }
        })
    }

    private fun abrirEnvioDeTarea(mission: Mission) {
        val intent = Intent(requireContext(), com.matrix.appmobilechimera.ui.mission.MissionSubmissionActivity::class.java)
        intent.putExtra("MISSION_DATA", mission)
        startActivity(intent)
    }
}
package com.matrix.appmobilechimera.ui.course

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.data.api.RetrofitClient
import com.matrix.appmobilechimera.model.Discussion
import com.matrix.appmobilechimera.ui.adapter.DiscussionAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

        // CORRECCIÓN: Nombres de funciones sin guion bajo
        val cursoId = intent.getIntExtra("CURSO_ID", -1)
        val cursoName = intent.getStringExtra("CURSO_NAME")

        findViewById<TextView>(R.id.tvForumTitle).text = "Foro: $cursoName"

        val rvDiscussions = findViewById<RecyclerView>(R.id.rvDiscussions)
        val progressBar = findViewById<ProgressBar>(R.id.pbForum)
        rvDiscussions.layoutManager = LinearLayoutManager(this)

        if (cursoId != -1) {
            cargarDiscusiones(cursoId, rvDiscussions, progressBar)
        }
    }

    private fun cargarDiscusiones(cursoId: Int, rv: RecyclerView, pb: ProgressBar) {
        pb.visibility = View.VISIBLE
        RetrofitClient.instance.getForumDiscussions(cursoId).enqueue(object : Callback<List<Discussion>> {
            override fun onResponse(call: Call<List<Discussion>>, response: Response<List<Discussion>>) {
                pb.visibility = View.GONE
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    rv.adapter = DiscussionAdapter(list) { discussion ->
                        // Por ahora solo un aviso (Alcance 5.2)
                        Toast.makeText(this@ForumActivity, "Leyendo: ${discussion.title}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Discussion>>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(this@ForumActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
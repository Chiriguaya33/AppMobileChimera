package com.matrix.appmobilechimera.ui.mission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.matrix.appmobilechimera.R
import com.matrix.appmobilechimera.data.api.RetrofitClient
import com.matrix.appmobilechimera.model.Mission
import com.matrix.appmobilechimera.model.Module
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class MissionSubmissionActivity : AppCompatActivity() {

    private var selectedFileUri: Uri? = null
    private var missionId: Int = -1

    private lateinit var tvMissionName: TextView
    private lateinit var etComment: EditText
    private lateinit var btnPickFile: Button
    private lateinit var tvFileName: TextView
    private lateinit var btnSubmit: Button
    private lateinit var progressBar: ProgressBar

    // Herramienta moderna para seleccionar archivos (ActivityResultLauncher)
    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedFileUri = result.data?.data
            tvFileName.text = getFileName(selectedFileUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission_submission)

        // 1. Inicializar vistas
        tvMissionName = findViewById(R.id.tvSubmissionMissionName)
        etComment = findViewById(R.id.etSubmissionComment)
        btnPickFile = findViewById(R.id.btnPickFile)
        tvFileName = findViewById(R.id.tvSelectedFileName)
        btnSubmit = findViewById(R.id.btnSubmitMission)
        progressBar = findViewById(R.id.pbSubmission)

        // 2. Recibir datos (Flexible: puede venir de Missions o de un Curso)
        val mission = intent.getParcelableExtra<Mission>("MISSION_DATA")
        val module = intent.getParcelableExtra<Module>("MODULE_DATA")

        missionId = mission?.id ?: module?.instance ?: -1
        tvMissionName.text = mission?.title ?: module?.name ?: "Tarea"

        // 3. Listeners
        btnPickFile.setOnClickListener { openFilePicker() }
        btnSubmit.setOnClickListener { enviarTarea() }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Permite cualquier tipo de archivo (PDF, Imágenes, etc.)
        filePickerLauncher.launch(intent)
    }

    private fun enviarTarea() {
        val sharedPref = getSharedPreferences("ChimeraSession", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("USER_ID", -1)
        val comentario = etComment.text.toString()

        if (userId == -1 || missionId == -1) {
            Toast.makeText(this, "Error de sesión o misión", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        btnSubmit.isEnabled = false

        // Convertir textos a RequestBody para Multipart
        val rbUserId = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val rbMissionId = missionId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val rbComentario = comentario.toRequestBody("text/plain".toMediaTypeOrNull())

        // Preparar el archivo (si existe)
        var filePart: MultipartBody.Part? = null
        selectedFileUri?.let { uri ->
            val file = uriToFile(uri)
            val requestFile = file.asRequestBody(contentResolver.getType(uri)?.toMediaTypeOrNull())
            filePart = MultipartBody.Part.createFormData("archivo", file.name, requestFile)
        }

        // Llamada a la API de Python
        RetrofitClient.instance.enviarMision(rbUserId, rbMissionId, rbComentario, filePart)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@MissionSubmissionActivity, "¡Tarea enviada con éxito!", Toast.LENGTH_LONG).show()
                        finish() // Cerramos y volvemos atrás
                    } else {
                        btnSubmit.isEnabled = true
                        Toast.makeText(this@MissionSubmissionActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    btnSubmit.isEnabled = true
                    Toast.makeText(this@MissionSubmissionActivity, "Fallo de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // --- FUNCIONES AUXILIARES (TÉCNICAS) ---

    private fun getFileName(uri: Uri?): String {
        var name = "Ningún archivo seleccionado"
        uri?.let {
            contentResolver.query(it, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, getFileName(uri))
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        return file
    }
}
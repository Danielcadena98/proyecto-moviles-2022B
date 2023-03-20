package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActualizarIngredienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_ingrediente)
        var id = intent.getStringExtra("proeyctoId")
        var tareaId = intent.getStringExtra("tareaId")
        var nombre = intent.getStringExtra("tareaNombre")
        var descripcion = intent.getStringExtra("tareaDescripcion")
        var terminada = intent.getStringExtra("tareaTerminada")


        var textNombre = this.findViewById<EditText>(R.id.tit_nombre_tarea)
        textNombre.setText(nombre)
        var textDescripcion= this.findViewById<EditText>(R.id.tit_descripcion_tarea)
        textDescripcion.setText(descripcion.toString())
        var textTerminada = this.findViewById<EditText>(R.id.tit_terminada)
        textTerminada.setText(terminada)


        val btnGuardarEstudiante = this.findViewById<Button>(R.id.btn_guardar_tarea)
        btnGuardarEstudiante.setOnClickListener {
            if (!checkChange(
                    nombre,
                    descripcion,
                    terminada,
                    textNombre.text.toString(),
                    textDescripcion.text.toString(),
                    textTerminada.text.toString()

                )
            ) {

                saveData(
                    id!!,
                    tareaId!!,
                    textNombre.text.toString(),
                    textDescripcion.text.toString(),
                    textTerminada.text.toString(),
                )
            }
            abrirActividadConParametros(IngredienteListActivity::class.java, it, id!!)

        }


    }

    private fun saveData(
        id: String,
        tareaId: String,
        textNombre: String,
        textDescripcion: String,
        textTerminada: String
    ) {

        val db = Firebase.firestore
        val users = db.collection("proyecto")
        val tareasCollectionsRef = users.document(id!!).collection("tarea")
        val dataTarea = hashMapOf(
            "id" to tareaId,
            "tareaNombre" to textNombre,
            "tareaDescripcion" to textDescripcion,
            "tareaTerminada" to textTerminada
        )
        tareasCollectionsRef.document(tareaId).set(dataTarea)

    }

    private fun checkChange(
        nombre: String?,
        descripcion: String?,
        terminada: String?,
        textNombre: String,
        textDescripcion: String,
        textTerminada: String
    ): Boolean {
        return (nombre == textNombre && descripcion == textDescripcion && terminada == textTerminada)
    }

    private val contenidoIntentExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            if (result.data != null) {
                val data = result.data
                Log.i("Intente-epn", "${data?.getStringExtra("nombreModificado")}")
            }
        }
    }

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, idColegio: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("colegioId", idColegio)
        contenidoIntentExplicito.launch(intent)
    }


    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
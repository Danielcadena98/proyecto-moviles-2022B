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
        var id = intent.getStringExtra("recetaId")
        var ingredienteId = intent.getStringExtra("ingredienteId")
        var nombre = intent.getStringExtra("ingredienteNombre")
        var descripcion = intent.getStringExtra("ingredienteDescripcion")
        var conseguido = intent.getStringExtra("ingredienteConseguido")


        var textNombre = this.findViewById<EditText>(R.id.tit_nombre_ingrediente)
        textNombre.setText(nombre)
        var textPreparacion= this.findViewById<EditText>(R.id.tit_descripcion_ingrediente)
        textPreparacion.setText(descripcion.toString())
        var textConseguido = this.findViewById<EditText>(R.id.tit_conseguido)
        textConseguido.setText(conseguido)


        val btnGuardarEstudiante = this.findViewById<Button>(R.id.btn_guardar_ingrediente)
        btnGuardarEstudiante.setOnClickListener {
            if (!checkChange(
                    nombre,
                    descripcion,
                    conseguido,
                    textNombre.text.toString(),
                    textPreparacion.text.toString(),
                    textConseguido.text.toString()

                )
            ) {

                saveData(
                    id!!,
                    ingredienteId!!,
                    textNombre.text.toString(),
                    textPreparacion.text.toString(),
                    textConseguido.text.toString(),
                )
            }
            abrirActividadConParametros(IngredienteListActivity::class.java, it, id!!)

        }


    }

    private fun saveData(
        id: String,
        ingredienteId: String,
        textNombre: String,
        textPreparacion: String,
        textConseguido: String
    ) {

        val db = Firebase.firestore
        val users = db.collection("receta")
        val ingredientesCollectionsRef = users.document(id!!).collection("ingrediente")
        val dataIngrediente= hashMapOf(
            "id" to ingredienteId,
            "ingredienteNombre" to textNombre,
            "ingredienteDescripcion" to textPreparacion,
            "ingredienteConseguido" to textConseguido
        )
        ingredientesCollectionsRef.document(ingredienteId).set(dataIngrediente)

    }

    private fun checkChange(
        nombre: String?,
        descripcion: String?,
        conseguido: String?,
        textNombre: String,
        textPreparacion: String,
        textConseguido: String
    ): Boolean {
        return (nombre == textNombre && descripcion == textPreparacion && conseguido == textConseguido)
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

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, idReceta: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("recetaId", idReceta)
        contenidoIntentExplicito.launch(intent)
    }


    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
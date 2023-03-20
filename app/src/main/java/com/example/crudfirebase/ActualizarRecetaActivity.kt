package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActualizarRecetaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_receta)
        var id = intent.getStringExtra("recetaId")
        val recetaNombre = intent.getStringExtra("recetaNombre")
        val recetaPreparacion = intent.getStringExtra("recetaPreparacion")

        var textnombreReceta = this.findViewById<EditText>(R.id.tit_nombre_receta)
        var textPreparacion = this.findViewById<EditText>(R.id.tit_preparacion)


        textnombreReceta.setText(recetaNombre)
        textPreparacion.setText(recetaPreparacion)

        val btnSaveData = this.findViewById<Button>(R.id.btn_save)
        btnSaveData.setOnClickListener {


            if (!checkChanges(
                    recetaNombre,
                    recetaPreparacion,
                    textnombreReceta.text.toString(),
                    textPreparacion.text.toString(),
                )
            ) {

                saveData(
                    id!!,
                    textnombreReceta.text.toString(),
                    textPreparacion.text.toString(),
                )
            }
            irActividad(RecetaListActivity::class.java)
        }


    }

    private fun saveData(
        id: String,
        textnombreReceta: String,
        textPreparacion: String,
    ) {
        val db = Firebase.firestore
        val receta = db.collection("receta")

        val data1 = hashMapOf(
            "id" to id,
            "nombreReceta" to textnombreReceta,
            "preparacionReceta" to textPreparacion,
           )
        receta.document(id).set(data1)
    }

    private fun checkChanges(
        recetaNombre: String?,
        recetaPreparacion: String?,
        textnombreReceta: String,
        textPreparacion: String?,
    ): Boolean {
        return (recetaNombre == textnombreReceta && recetaPreparacion == textPreparacion)
    }


    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


}
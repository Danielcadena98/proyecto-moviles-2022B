package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ActualizarRecetaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_receta)
        var id = intent.getStringExtra("proyectoId")
        val proyectoNombre = intent.getStringExtra("proyectoNombre")
        val proyectoDescripcion = intent.getStringExtra("proyectoDescripcion")
        val proyectoFecha = intent.getStringExtra("proyectoFecha")

        var textNombreProyecto = this.findViewById<EditText>(R.id.tit_nombre_receta)
        var textDescripcion = this.findViewById<EditText>(R.id.tit_ingredientes)
        var textFecha = this.findViewById<EditText>(R.id.tit_fecha)


        textNombreProyecto.setText(proyectoNombre)
        textDescripcion.setText(proyectoDescripcion)
        textFecha.setText(proyectoFecha)

        val btnSaveData = this.findViewById<Button>(R.id.btn_save)
        btnSaveData.setOnClickListener {


            if (!checkChanges(
                    proyectoNombre,
                    proyectoDescripcion,
                    proyectoFecha,
                    textNombreProyecto.text.toString(),
                    textDescripcion.text.toString(),
                    textFecha.text.toString()
                )
            ) {

                if (id == null) {
                    id = Date().time.toString()
                }
                saveData(
                    id!!,
                    textNombreProyecto.text.toString(),
                    textDescripcion.text.toString(),
                    textFecha.text.toString()
                )
            }
            irActividad(RecetaListActivity::class.java)
        }


    }

    private fun saveData(
        id: String,
        textNombreProyecto: String,
        textDescripcion: String,
        textFecha: String
    ) {
        val db = Firebase.firestore
        val proyecto = db.collection("proyecto")

        val data1 = hashMapOf(
            "id" to id,
            "nombreProyecto" to textNombreProyecto,
            "descripcionProyecto" to textDescripcion,
            "fechaProyecto" to textFecha,
           )
        proyecto.document(id).set(data1)
    }

    private fun checkChanges(
        proyectoNombre: String?,
        proyectoDescripcion: String?,
        proyectoFecha: String?,
        textNombreProyecto: String,
        textDescripcion: String?,
        textFecha: String
    ): Boolean {
        return (proyectoNombre == textNombreProyecto && proyectoDescripcion == textDescripcion && proyectoFecha == textFecha)
    }


    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


}
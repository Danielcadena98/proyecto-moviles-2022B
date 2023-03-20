package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class RecetaListActivity : AppCompatActivity() {


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


    private lateinit var userRecyclerView: RecyclerView
    private lateinit var recetaArrayList: ArrayList<Receta>
    private lateinit var adaptador: AdapterReceta


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receta_list)

        userRecyclerView = findViewById(R.id.recetaListRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        recetaArrayList = arrayListOf<Receta>()
        adaptador = AdapterReceta(recetaArrayList, contenidoIntentExplicito)
        userRecyclerView.adapter = adaptador

        consultarDocumentos()

        val btnCreateData = this.findViewById<Button>(R.id.btn_crear_receta)
         btnCreateData.setOnClickListener {
             irActividad(ActualizarRecetaActivity::class.java)
         }






    }





    private fun consultarDocumentos(){
        val db = Firebase.firestore
        val proyectoRef = db.collection("proyecto")
        limpiarArreglo()
        proyectoRef.get().addOnSuccessListener { result ->
            for (document in result) {
                val receta = Receta(
                    document.get("id") as String?,
                    document.get("nombreProyecto") as String?,
                    document.get("descripcionProyecto") as String?,
                    document.get("fechaProyecto") as String?
                )
                recetaArrayList.add(receta)
            }
            // Actualizar la lista de usuarios en la vista
            adaptador.recetaList = recetaArrayList
            adaptador.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.d(null, "Error al obtener proyecto", exception)
        }
    }



    private fun limpiarArreglo() {
        this.recetaArrayList.clear()
    }

    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }





}
package com.example.crudfirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class IngredienteListActivity : AppCompatActivity() {

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

    private lateinit var tareaListRecyclerView: RecyclerView
    private lateinit var ingredienteArrayList: ArrayList<Ingrediente>
    private lateinit var adapterIngrediente: AdapterIngrediente

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingrediente_list)

        var id = intent.getStringExtra("proyectoId")

        tareaListRecyclerView = findViewById(R.id.tareaListRecyclerView)
        tareaListRecyclerView.layoutManager = LinearLayoutManager(this)
        tareaListRecyclerView.setHasFixedSize(true)
        ingredienteArrayList = arrayListOf<Ingrediente>()
        if (id != null) {
            consultarDocumentosProyecto(id!!)
        }
        adapterIngrediente = AdapterIngrediente(ingredienteArrayList, contenidoIntentExplicito, id!!)
        tareaListRecyclerView.adapter = adapterIngrediente

        val btnNuevaTarea = findViewById<Button>(R.id.btn_nueva_tarea)
        btnNuevaTarea.setOnClickListener {
            abrirActividadConParametros(ActualizarIngredienteActivity::class.java, it, id!!)
        }
    }

    private fun consultarDocumentosProyecto(id: String) {

        val db = Firebase.firestore
        val proyectoRef = db.collection("proyecto").document(id)
        val tareaCollectionRef = proyectoRef.collection("tarea")
        limpiarArreglo()
        tareaCollectionRef.get().addOnSuccessListener { querySnaps ->
            for (document in querySnaps.documents) {
                val ingrediente = Ingrediente(
                    document.get("id") as String?,
                    document.get("tareaNombre") as String?,
                    document.get("tareaDescripcion") as String?,
                    document.get("tareaTerminada") as String?
                )
                    this.ingredienteArrayList.add(ingrediente)

            }
            adapterIngrediente.ingredienteList = ingredienteArrayList
            adapterIngrediente.notifyDataSetChanged()
        }

    }

    private fun limpiarArreglo() {
        this.ingredienteArrayList.clear()
    }

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, idColegio: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("colegioId", idColegio)
        contenidoIntentExplicito.launch(intent)
    }
}
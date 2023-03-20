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

    private lateinit var ingredienteListRecyclerView: RecyclerView
    private lateinit var ingredienteArrayList: ArrayList<Ingrediente>
    private lateinit var adapterIngrediente: AdapterIngrediente

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingrediente_list)

        var id = intent.getStringExtra("recetaId")

        ingredienteListRecyclerView = findViewById(R.id.ingredienteListRecyclerView)
        ingredienteListRecyclerView.layoutManager = LinearLayoutManager(this)
        ingredienteListRecyclerView.setHasFixedSize(true)
        ingredienteArrayList = arrayListOf<Ingrediente>()
        if (id != null) {
            consultarDocumentosReceta(id!!)
        }
        adapterIngrediente = AdapterIngrediente(ingredienteArrayList, contenidoIntentExplicito, id!!)
        ingredienteListRecyclerView.adapter = adapterIngrediente

        val btnNuevaIngrediente= findViewById<Button>(R.id.btn_nuevo_ingrediente)
        btnNuevaIngrediente.setOnClickListener {
            abrirActividadConParametros(ActualizarIngredienteActivity::class.java, it, id!!)
        }
    }

    private fun consultarDocumentosReceta(id: String) {

        val db = Firebase.firestore
        val recetaRef = db.collection("receta").document(id)
        val ingredienteCollectionRef = recetaRef.collection("ingrediente")
        limpiarArreglo()
        ingredienteCollectionRef.get().addOnSuccessListener { querySnaps ->
            for (document in querySnaps.documents) {
                val ingrediente = Ingrediente(
                    document.get("id") as String?,
                    document.get("ingredienteNombre") as String?,
                    document.get("ingredienteDescripcion") as String?,
                    document.get("ingredienteConseguido") as String?
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

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, idReceta: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("recetaId", idReceta)
        contenidoIntentExplicito.launch(intent)
    }
}
package com.example.crudfirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterIngrediente(
    var ingredienteList: ArrayList<Ingrediente>,
    private val contenidoIntentExplicito: ActivityResultLauncher<Intent>,
    val idReceta: String
) : RecyclerView.Adapter<AdapterIngrediente.MyViewHolderIngrediente>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderIngrediente{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ingrediente_item, parent, false)
        return MyViewHolderIngrediente(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolderIngrediente, position: Int) {
        val ingredienteActual = ingredienteList[position]
        holder.idIngrediente.text = ingredienteActual.id
        holder.nombreIngrediente.text = ingredienteActual.ingredienteNombre
        holder.descripcionIngrediente.text = ingredienteActual.ingredienteDescripcion
        holder.conseguidoIngrediente.text = ingredienteActual.ingredienteConseguido
        holder.btnEditarIngrediente.setOnClickListener {
            abrirActividadConParametros(ActualizarIngredienteActivity::class.java, ingredienteActual, it, idReceta)
        }
        holder.btnEditarIngrediente.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirmar eliminación")
            builder.setMessage("Estás seguro que lo quieres eliminar?")
            builder.setPositiveButton("Si") { dialog, _ ->
                dialog.dismiss()
                deleteTarea(ingredienteActual.id!!, idReceta)
                this.ingredienteList.remove(ingredienteActual)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

    }

        private fun deleteTarea(id: String, idReceta: String) {
        val db = Firebase.firestore
        val users = db.collection("receta")
        val ingredienteCollectionsRef = users.document(idReceta).collection("ingrediente")
        ingredienteCollectionsRef.document(id).delete().addOnSuccessListener {

        }.addOnFailureListener {

        }



    }

    override fun getItemCount(): Int {
        return this.ingredienteList.size
    }

    private fun abrirActividadConParametros(clase: Class<*>, ingrediente: Ingrediente, it: View?, idReceta: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("recetaId", ingrediente!!.id)
        intent.putExtra("ingredienteNombre", ingrediente!!.ingredienteNombre)
        intent.putExtra("recetaId", idReceta)
        intent.putExtra("ingredienteDescripcion", ingrediente!!.ingredienteDescripcion)
        intent.putExtra("ingredienteConseguido", ingrediente!!.ingredienteConseguido)
        contenidoIntentExplicito.launch(intent)
    }

    class MyViewHolderIngrediente(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idIngrediente= itemView.findViewById<TextView>(R.id.tv_id_ingrediente)
        val nombreIngrediente= itemView.findViewById<TextView>(R.id.tv_nombre_ingrediente)
        val btnEliminarIngrediente= itemView.findViewById<Button>(R.id.btn_eliminar_ingrediente)
        val btnEditarIngrediente= itemView.findViewById<Button>(R.id.btn_editar_ingrediente)
        val conseguidoIngrediente= itemView.findViewById<TextView>(R.id.tv_conseguido)
        val descripcionIngrediente= itemView.findViewById<TextView>(R.id.tv_descripcion_ingrediente)
    }


}
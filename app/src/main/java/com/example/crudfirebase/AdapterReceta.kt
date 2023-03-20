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


class AdapterReceta(var recetaList: ArrayList<Receta>, private val contenidoIntentExplicito: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<AdapterReceta.MyViewHolderUser>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderUser {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.receta_item, parent, false)

        return MyViewHolderUser(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolderUser, position: Int) {
        val currentItem = recetaList[position]
        holder.idReceta.text = currentItem.id
        holder.nombreReceta.text = currentItem.nombreReceta
        holder.descripcion.text = currentItem.preparacionReceta
        holder.btnEditar.setOnClickListener {
            abrirActividadConParametros(ActualizarRecetaActivity::class.java, currentItem, it)
        }


        holder.btnEliminar.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirmar")
            builder.setMessage("¿Deseas eliminar este receta?")
            builder.setPositiveButton("Si") { dialog, _ ->
                // Delete the item
                dialog.dismiss()
                // Perform the deletion here
                deleteReceta(currentItem.id!!)
                this.recetaList.remove(currentItem)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        holder.btnVerTareas.setOnClickListener {
            abrirActividadConParametros(IngredienteListActivity::class.java, currentItem, it)
        }
    }

    private fun deleteReceta(id: String) {
        val db = Firebase.firestore
        val receta = db.collection("receta")
        val recetaDoc = receta.document(id)
        recetaDoc.delete().addOnSuccessListener {

        }.addOnFailureListener {

        }


    }

    private fun abrirActividadConParametros(clase: Class<*>, receta: Receta, it: View?) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("recetaId", receta!!.id)
        intent.putExtra("recetaNombre", receta!!.nombreReceta)
        intent.putExtra("recetaPreparacion", receta!!.preparacionReceta)
        contenidoIntentExplicito.launch(intent)
    }

    override fun getItemCount(): Int {
        return recetaList.size
    }

    

    class MyViewHolderUser(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreReceta = itemView.findViewById<TextView>(R.id.tv_nombre_receta)
        val btnEditar = itemView.findViewById<Button>(R.id.btn_editar)
        val btnEliminar = itemView.findViewById<Button>(R.id.btn_eliminar)
        val btnVerTareas = itemView.findViewById<Button>(R.id.btn_ver_ingredientes)
        val idReceta = itemView.findViewById<TextView>(R.id.tvIdReceta)
        val descripcion = itemView.findViewById<TextView>(R.id.tv_preparacion)
    }


}
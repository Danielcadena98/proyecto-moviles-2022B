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
    val idProyecto: String
) : RecyclerView.Adapter<AdapterIngrediente.MyViewHolderTarea>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderTarea {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ingrediente_item, parent, false)
        return MyViewHolderTarea(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolderTarea, position: Int) {
        val tareaActual = ingredienteList[position]
        holder.idTarea.text = tareaActual.id
        holder.nombreTarea.text = tareaActual.tareaNombre
        holder.descripcionTarea.text = tareaActual.tareaDescripcion
        holder.terminadaTarea.text = tareaActual.tareaTerminada
        holder.btnEditarTarea.setOnClickListener {
            abrirActividadConParametros(ActualizarIngredienteActivity::class.java, tareaActual, it, idProyecto)
        }
        holder.btnEditarTarea.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirmar eliminación")
            builder.setMessage("Estás seguro que lo quieres eliminar?")
            builder.setPositiveButton("Si") { dialog, _ ->
                dialog.dismiss()
                deleteTarea(tareaActual.id!!, idProyecto)
                this.ingredienteList.remove(tareaActual)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

    }

        private fun deleteTarea(id: String, idProyecto: String) {
        val db = Firebase.firestore
        val users = db.collection("proyecto")
        val tareaCollectionsRef = users.document(idProyecto).collection("tarea")
        tareaCollectionsRef.document(id).delete().addOnSuccessListener {

        }.addOnFailureListener {

        }



    }

    override fun getItemCount(): Int {
        return this.ingredienteList.size
    }

    private fun abrirActividadConParametros(clase: Class<*>, ingrediente: Ingrediente, it: View?, idProyecto: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("proeyctoId", ingrediente!!.id)
        intent.putExtra("tareaNombre", ingrediente!!.tareaNombre)
        intent.putExtra("proyectoId", idProyecto)
        intent.putExtra("tareaDescripcion", ingrediente!!.tareaDescripcion)
        intent.putExtra("tareaTerminada", ingrediente!!.tareaTerminada)
        contenidoIntentExplicito.launch(intent)
    }

    class MyViewHolderTarea(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idTarea = itemView.findViewById<TextView>(R.id.tv_id_ingrediente)
        val nombreTarea = itemView.findViewById<TextView>(R.id.tv_nombre_ingrediente)
        val btnEliminarTarea = itemView.findViewById<Button>(R.id.btn_eliminar_ingrediente)
        val btnEditarTarea = itemView.findViewById<Button>(R.id.btn_editar_ingrediente)
        val terminadaTarea = itemView.findViewById<TextView>(R.id.tv_conseguido)
        val descripcionTarea = itemView.findViewById<TextView>(R.id.tv_descripcion_ingrediente)
    }


}
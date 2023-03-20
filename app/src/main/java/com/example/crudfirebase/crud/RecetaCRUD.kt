package com.example.aplicacioncocina.crud

import com.example.aplicacioncocina.entities.Receta
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class RecetaCRUD : AbstractCRUD<Receta> {
    val db = Firebase.firestore
    override fun create(obj: Receta, onSuccess: (String) -> Unit) {
        db.collection("recetas")
            .add(obj)
            .addOnSuccessListener { documentReference ->
                val id = documentReference.id
                db.collection("recetas").document(id)
                    .update("idReceta", id)
                    .addOnSuccessListener {
                        onSuccess(id)
                    }
                    .addOnFailureListener { exception ->
                        onSuccess(exception.message.toString())
                    }
            }
            .addOnFailureListener { exception ->
                onSuccess(exception.message.toString())
            }
    }

    override fun read(onSuccess: (List<Receta>) -> Unit) {
        db.collection("recetas")
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Receta>()
                for (document in result) {
                    if (document != null) {
                        val receta = document.toObject(Receta::class.java)
                        list.add(receta)
                    }
                }
                onSuccess(list)
            }
            .addOnFailureListener() {
                onSuccess(listOf())
            }
    }

    override fun update(obj: Receta, onSuccess: (String) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun delete(id: String, onSuccess: (String) -> Unit) {
        // Eliminamos todas las colecciones que tenga la receta
        db.collection("recetas").document(id).collection("ingredientes").get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document != null) {
                        db.collection("recetas").document(id).collection("ingredientes")
                            .document(document.id).delete()
                    }
                }
            }
    }

    override fun getById(id: String, onSuccess: (Receta) -> Unit) {
        TODO("Not yet implemented")
    }

}
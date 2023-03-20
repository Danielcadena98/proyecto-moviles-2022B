package com.example.aplicacioncocina.crud

import com.example.aplicacioncocina.entities.Ingrediente
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class IngredienteCRUD : AbstractCRUD<Ingrediente> {
    private val db = Firebase.firestore

    override fun create(obj: Ingrediente, onSuccess: (String) -> Unit) {
        db.collection("recetas").document(obj.idReceta)
            .collection("ingredientes")
            .add(obj)
            .addOnSuccessListener {
                val id = it.id
                db.collection("recetas").document(obj.idReceta)
                    .collection("ingredientes")
                    .document(id)
                    .update("idIngrediente", id)
                    .addOnSuccessListener {
                    onSuccess("Ingrediente creado correctamente")
                }.addOnFailureListener {
                    onSuccess("Error al crear el ingrediente")
                }
            }.addOnFailureListener {
            onSuccess("Error al crear el ingrediente")
        }
    }

    override fun read(onSuccess: (List<Ingrediente>) -> Unit) {
        db.collection("recetas")
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Ingrediente>()
                for (document in result) {
                    if(document != null){
                        val receta = document.toObject(Ingrediente::class.java)
                        list.add(receta)
                    }
                }
                onSuccess(list)
            }
            .addOnFailureListener() {
                onSuccess(listOf())
            }
    }

    fun createByRecetaId(idReceta:String, obj: Ingrediente, onSuccess: (String) -> Unit) {
        db.collection("recetas").document(idReceta)
            .collection("ingredientes")
            .add(obj)
            .addOnSuccessListener {
                val id = it.id
                db.collection("recetas").document(idReceta)
                    .collection("ingredientes")
                    .document(id)
                    .update("idIngrediente", id)
                    .addOnSuccessListener {
                        onSuccess("Ingrediente creado correctamente")
                    }.addOnFailureListener {
                        onSuccess("Error al crear el ingrediente")
                    }
            }.addOnFailureListener {
                onSuccess("Error al crear el ingrediente")
            }
    }

    override fun update(obj: Ingrediente, onSuccess: (String) -> Unit) {
        db.collection("recetas").document(obj.idReceta)
            .collection("ingredientes")
            .document(obj.idIngrediente)
            .set(obj)
            .addOnSuccessListener {
                onSuccess("Ingrediente actualizado correctamente")
            }.addOnFailureListener {
                onSuccess("Error al actualizar el ingrediente")
            }
    }

    override fun delete(id: String, onSuccess: (String) -> Unit) {
       db.collection("recetas").document(id).collection("ingredientes").get().addOnSuccessListener {
            for (document in it) {
                if(document != null){
                    db.collection("recetas").document(id).collection("ingredientes").document(document.id).delete()
                }
            }
        }
        onSuccess("Ingrediente eliminado correctamente")

    }

    override fun getById(id: String, onSuccess: (Ingrediente) -> Unit) {
        TODO("Not yet implemented")
    }
}

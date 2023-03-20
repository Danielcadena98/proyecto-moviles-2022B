package com.example.aplicacioncocina.crud

interface  AbstractCRUD<T> {
    fun create(obj:T, onSuccess: (String) -> Unit)
    fun read(onSuccess: (List<T>) -> Unit)
    fun update(obj:T, onSuccess: (String) -> Unit)
    fun delete(id:String, onSuccess: (String) -> Unit)
    fun getById(id:String, onSuccess: (T) -> Unit)
}
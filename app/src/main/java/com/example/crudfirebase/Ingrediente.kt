package com.example.crudfirebase

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Ingrediente(
    val id: String? = null,
    var ingredienteNombre: String? = null,
    var ingredienteDescripcion: String? = null,
    var ingredienteConseguido: String? = ""

):Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel):this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(ingredienteNombre)
        parcel.writeString(ingredienteDescripcion)
        parcel.writeString(ingredienteConseguido)
    }
    companion object CREATOR : Parcelable.Creator<Ingrediente> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Ingrediente {
            return Ingrediente(parcel)
        }

        override fun newArray(size: Int): Array<Ingrediente?> {
            return arrayOfNulls(size)
        }
    }
    //
}
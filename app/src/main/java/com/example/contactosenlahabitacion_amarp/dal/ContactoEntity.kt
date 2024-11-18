package com.example.contactosenlahabitacion_amarp.dal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contactos")
data class ContactoEntity(
    @PrimaryKey
    var telefono:String,
    var nombre:String,
    var apellido:String,
    var foto:Int
)

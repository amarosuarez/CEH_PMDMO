package com.example.contactosenlahabitacion_amarp.dal

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ContactoEntity::class], version = 1, exportSchema = true)
abstract class ContactoDatabase : RoomDatabase() {
    abstract fun contactoDao(): ContactoDao

}
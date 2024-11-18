package com.example.contactosenlahabitacion_amarp.dal

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactoDao {
    // Obtiene todos los contactos
    @Query("SELECT * FROM contactos")
    suspend fun getAll(): List<ContactoEntity>

    // Comprueba si un telefono ya está registrado
    @Query("SELECT COUNT(*) FROM contactos WHERE telefono = :telefono")
    suspend fun existsByTlfn(telefono: String): Boolean

    // Inserta un usuario, reemplazando en caso de conflicto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: ContactoEntity): Long

    // Actualiza un usuario
    @Update
    suspend fun update(user: ContactoEntity)

    // Elimina un usuario
    @Delete
    suspend fun delete(user: ContactoEntity)
}
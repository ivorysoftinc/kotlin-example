package com.ivorysoftinc.kotlin.example.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

/**
 * [CardsDao] represents interface for saving Cards.
 */
@Dao
interface CardsDao {

    /**
     * [Transaction] of clearing table and inserting new items.
     */
    @Transaction
    suspend fun updateCards(newList: List<CardEntity>) {
        nukeTable()
        insert(newList)
    }

    @Insert
    suspend fun insert(list: List<CardEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun nukeTable()

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getCards(): List<CardEntity>

    companion object {
        const val TABLE_NAME = "cards"
    }
}

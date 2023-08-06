package com.iulian.iancu.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelQueryDAO {

    @Upsert
    fun insertQuery(query: HotelQuery)

    @Query("SELECT * FROM HotelQuery")
    fun getAllQueries(): Flow<List<HotelQuery>>
}
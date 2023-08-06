package com.iulian.iancu.data.storage

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iulian.iancu.domain.HotelEntity

@Entity
data class HotelQuery(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val checkInDate: String,
    val checkOutDate: String,
    val nrAdults: Int,
    val nrChildren: Int,
    val results: List<HotelEntity>
)
package com.iulian.iancu.domain

import kotlinx.coroutines.flow.Flow

interface HotelRepository {

    suspend fun getNewHotels(
        checkInDate: String, checkOutDate: String, nrAdults: Int, nrChildren: Int,
    ): List<HotelEntity>

    suspend fun getPreviousSearches(): Flow<List<HotelQueryEntity>>
}
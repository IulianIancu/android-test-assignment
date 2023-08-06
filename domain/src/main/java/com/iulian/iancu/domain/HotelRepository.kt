package com.iulian.iancu.domain

import kotlinx.coroutines.flow.Flow

interface HotelRepository {

    suspend fun getNewHotels(): Flow<List<HotelEntity>>
    suspend fun getPreviousSearch():Flow<List<HotelEntity>>
}
package com.iulian.iancu.domain

import kotlinx.coroutines.flow.Flow

class GetNewHotelsUseCase(private val hotelRepository: HotelRepository) {
    private suspend fun run(): Flow<List<HotelEntity>> {
        return hotelRepository.getNewHotels()
    }

    suspend operator fun invoke(): Flow<List<HotelEntity>> {
        return run()
    }
}
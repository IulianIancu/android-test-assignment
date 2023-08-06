package com.iulian.iancu.domain

import kotlinx.coroutines.flow.Flow

class GetPreviousSearchesUseCase(private val hotelRepository: HotelRepository) {
    private suspend fun run(): Flow<List<HotelQueryEntity>> {
        return hotelRepository.getPreviousSearches()
    }

    suspend operator fun invoke(): Flow<List<HotelQueryEntity>> {
        return run()
    }
}
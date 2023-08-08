package com.iulian.iancu.domain

import kotlinx.coroutines.flow.Flow

class GetNewHotelsUseCase(private val hotelRepository: HotelRepository) {
    private suspend fun run(
        checkInDate: String,
        checkOutDate: String,
        nrAdults: Int,
        nrChildren: Int,
    ): List<HotelEntity> {
        return hotelRepository.getNewHotels(checkInDate, checkOutDate, nrAdults, nrChildren)
    }

    suspend operator fun invoke(
        checkInDate: String,
        checkOutDate: String,
        nrAdults: Int,
        nrChildren: Int,
    ): List<HotelEntity> {
        return run(checkInDate, checkOutDate, nrAdults, nrChildren)
    }
}
package com.example.shacklehotelbuddy.di

import com.iulian.iancu.data.HotelRepositoryImpl
import com.iulian.iancu.data.network.HotelService
import com.iulian.iancu.data.storage.HotelQueryDAO
import com.iulian.iancu.data.storage.HotelQueryDatabase
import com.iulian.iancu.domain.GetNewHotelsUseCase
import com.iulian.iancu.domain.GetPreviousSearchesUseCase
import com.iulian.iancu.domain.HotelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class MainModule {

    @Provides
    fun provideGetNewHotelsUseCase(repository: HotelRepository): GetNewHotelsUseCase =
        GetNewHotelsUseCase(repository)

    @Provides
    fun provideGetPreviousSearchesUseCase(repository: HotelRepository): GetPreviousSearchesUseCase =
        GetPreviousSearchesUseCase(repository)

    @Provides
    fun provideHotelRepository(
        hotelDAO: HotelQueryDAO,
        hotelService: HotelService
    ): HotelRepository =
        HotelRepositoryImpl(hotelDAO, hotelService)

    @Provides
    fun provideHotelDAO(hotelDatabase: HotelQueryDatabase): HotelQueryDAO =
        hotelDatabase.hotelQueryDAO()

    @Provides
    fun provideHotelService(): HotelService = HotelService.getInstance()
}
package com.iulian.iancu.data

import android.accounts.NetworkErrorException
import com.iulian.iancu.data.network.CheckInDate
import com.iulian.iancu.data.network.CheckOutDate
import com.iulian.iancu.data.network.Child
import com.iulian.iancu.data.network.HotelSearchCriteria
import com.iulian.iancu.data.network.HotelService
import com.iulian.iancu.data.network.Property
import com.iulian.iancu.data.network.Room
import com.iulian.iancu.data.storage.HotelQuery
import com.iulian.iancu.data.storage.HotelQueryDAO
import com.iulian.iancu.domain.HotelEntity
import com.iulian.iancu.domain.HotelQueryEntity
import com.iulian.iancu.domain.HotelQueryEntity.Companion.DATE_FORMAT
import com.iulian.iancu.domain.HotelQueryEntity.Companion.DAY_FORMAT
import com.iulian.iancu.domain.HotelQueryEntity.Companion.MONTH_FORMAT
import com.iulian.iancu.domain.HotelQueryEntity.Companion.YEAR_FORMAT
import com.iulian.iancu.domain.HotelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HotelRepositoryImpl(
    private val hotelQueryDAO: HotelQueryDAO,
    private val hotelService: HotelService
) : HotelRepository {
    override suspend fun getNewHotels(
        checkInDate: String,
        checkOutDate: String,
        nrAdults: Int,
        nrChildren: Int
    ): List<HotelEntity> {
        val checkIn = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(checkInDate) as Date
        val checkOut =
            SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(checkOutDate) as Date

        val childList = mutableListOf<Child>()
        for (i in 1..nrChildren) {
            childList.add(Child(5))
        }

        val result = hotelService.getHotels(
            HotelSearchCriteria(
                checkInDate = CheckInDate(
                    SimpleDateFormat(DAY_FORMAT, Locale.getDefault()).format(checkIn),
                    SimpleDateFormat(MONTH_FORMAT, Locale.getDefault()).format(checkIn),
                    SimpleDateFormat(YEAR_FORMAT, Locale.getDefault()).format(checkIn)

                ),
                checkOutDate = CheckOutDate(
                    SimpleDateFormat(DAY_FORMAT, Locale.getDefault()).format(checkOut),
                    SimpleDateFormat(MONTH_FORMAT, Locale.getDefault()).format(checkOut),
                    SimpleDateFormat(YEAR_FORMAT, Locale.getDefault()).format(checkOut)
                ),
                rooms = listOf(Room(nrAdults, childList))
            )
        )

        val hotels = result.body()?.data?.propertySearch?.properties
        if (result.isSuccessful && !hotels.isNullOrEmpty()) {
            //Step 1: save the successful query to the database
            val latestHotelQuery = HotelQuery(
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                nrAdults = nrAdults,
                nrChildren = nrChildren,
                results = hotels.map { it.toHotelEntity() }
            )
            hotelQueryDAO.insertQuery(latestHotelQuery)

            //Step 2: Return the result from the API
            return hotels.map { it.toHotelEntity() }
        } else {
            throw NetworkErrorException("There was an issue with the API")
        }
    }

    override suspend fun getPreviousSearches(): Flow<List<HotelQueryEntity>> {
        return hotelQueryDAO.getAllQueries().map {
            it.map { hotelQuery ->
                HotelQueryEntity(
                    hotelQuery.checkInDate,
                    hotelQuery.checkOutDate,
                    hotelQuery.nrAdults,
                    hotelQuery.nrChildren,
                    hotelQuery.results
                )
            }
        }
    }

    private fun Property.toHotelEntity(): HotelEntity {
        return HotelEntity(
            this.name,
            this.propertyImage.image.url,
            this.neighborhood.name,
            this.reviews.score,
            this.price.lead.formatted
        )
    }
}
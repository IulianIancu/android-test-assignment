package com.iulian.iancu.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HotelSearchCriteria(
    @Expose
    @SerializedName("currency")
    val currency: String = "USD",
    @Expose
    @SerializedName("eapid")
    val eapid: Int = 1,
    @Expose
    @SerializedName("locale")
    val locale: String = "en_US",
    @Expose
    @SerializedName("siteId")
    val siteId: Int = 300000001,
    @Expose
    @SerializedName("destination")
    val destination: Destination = Destination(),
    @Expose
    @SerializedName("checkInDate")
    val checkInDate: CheckInDate,
    @Expose
    @SerializedName("checkOutDate")
    val checkOutDate: CheckOutDate,
    @Expose
    @SerializedName("rooms")
    val rooms: List<Room>,
    @Expose
    @SerializedName("resultsStartingIndex")
    val resultsStartingIndex: Int = 0,
    @Expose
    @SerializedName("resultsSize")
    val resultsSize: Int = 20,
    @Expose
    @SerializedName("sort")
    val sort: String = "PRICE_LOW_TO_HIGH",
    @Expose
    @SerializedName("filters")
    val filters: Filter = Filter()
)

data class Destination(
    @Expose
    @SerializedName("regionId")
    val regionId: String = "6054439"
)

data class CheckInDate(
    @Expose
    @SerializedName("day")
    val day: String,
    @Expose
    @SerializedName("month")
    val month: String,
    @Expose
    @SerializedName("year")
    val year: String
)

data class CheckOutDate(
    @Expose
    @SerializedName("day")
    val day: String,
    @Expose
    @SerializedName("month")
    val month: String,
    @Expose
    @SerializedName("year")
    val year: String
)

data class Room(
    @Expose
    @SerializedName("adults")
    val adults: Int,
    @Expose
    @SerializedName("children")
    val children: List<Child>
)

data class Child(
    @Expose
    @SerializedName("age")
    val age: Int
)

data class Filter(
    @Expose
    @SerializedName("price")
    val price: Price = Price()
)

data class Price(
    @Expose
    @SerializedName("max")
    val max: Int = 150,
    @Expose
    @SerializedName("min")
    val min: Int = 100
)

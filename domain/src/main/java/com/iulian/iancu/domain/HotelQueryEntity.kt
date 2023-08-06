package com.iulian.iancu.domain

data class HotelQueryEntity(
    val checkInDate: String,
    val checkOutDate: String,
    val nrAdults: Int,
    val nrChildren: Int,
    val results: List<HotelEntity>
){
    companion object{
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val DAY_FORMAT = "dd"
        const val MONTH_FORMAT = "MM"
        const val YEAR_FORMAT = "yyyy"
    }
}

package com.iulian.iancu.data.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface HotelService {

    @Headers(
        "Accept: application/json",
        "X-RapidAPI-Host: hotels4.p.rapidapi.com",
        "X-RapidAPI-Key: 656ba79213msh9dd99b1cb3441d3p170168jsn4301b0a24f0a"
    )
    @POST("list")
    suspend fun getHotels(@Body criteria: HotelSearchCriteria): Response<HotelsResponse>

    companion object {
        var retrofitService: HotelService? = null
        fun getInstance(): HotelService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://hotels4.p.rapidapi.com/properties/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(HotelService::class.java)
            }
            return retrofitService!!
        }
    }
}
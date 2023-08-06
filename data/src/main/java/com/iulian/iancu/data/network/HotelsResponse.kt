package com.iulian.iancu.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HotelsResponse(
    @Expose
    @SerializedName("data")
    val data: HotelData
)

data class HotelData(
    @Expose
    @SerializedName("propertySearch")
    val propertySearch: PropertySearch
)

data class PropertySearch(
    @Expose
    @SerializedName("properties")
    val properties: List<Property>
)

data class Property(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("propertyImage")
    val propertyImage: PropertyImage,
    @Expose
    @SerializedName("neighborhood")
    val neighborhood: Neighborhood,
    @Expose
    @SerializedName("reviews")
    val reviews: Reviews,
    @Expose
    @SerializedName("price")
    val price: LeadPrice
)

data class PropertyImage(
    @Expose
    @SerializedName("image")
    val image: Image
)

data class Image(
    @Expose
    @SerializedName("url")
    val url: String
)

data class Neighborhood(
    @Expose
    @SerializedName("name")
    val name: String
)

data class Reviews(
    @Expose
    @SerializedName("score")
    val score: Double
)

data class LeadPrice(
    @Expose
    @SerializedName("lead")
    val lead: Lead
)

data class Lead(
    @Expose
    @SerializedName("formatted")
    val formatted: String
)
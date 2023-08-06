package com.iulian.iancu.data.storage

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iulian.iancu.domain.HotelEntity

class HotelDataConverter {
    @TypeConverter
    fun fromEntityListToString(value: List<HotelEntity>): String {
        return Gson().toJson(value, object : TypeToken<List<HotelEntity>>() {}.type)
    }

    @TypeConverter
    fun fromStringToEntityList(value: String): List<HotelEntity> {
        return Gson().fromJson(value, object : TypeToken<List<HotelEntity>>() {}.type)
    }
}
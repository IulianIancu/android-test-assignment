package com.iulian.iancu.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [HotelQuery::class],
    version = 1
)
@TypeConverters(HotelDataConverter::class)
abstract class HotelQueryDatabase : RoomDatabase() {
    abstract fun hotelQueryDAO(): HotelQueryDAO
}
package com.androiddevs.mvvmnewsapp.db

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.api.Source

class Converters {

    @TypeConverter
    fun fromSourceToString(source: Source): String = source.name

    @TypeConverter
    fun fromStringToSource(name: String): Source = Source(name, name)
}
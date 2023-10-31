package io.b306.picashow.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.b306.picashow.converter.Converters
import java.util.Date

@Entity(tableName = "schedule")
@TypeConverters(Converters::class)
data class Schedule(
    @PrimaryKey(autoGenerate = true) val scheduleSeq : Long? = 0L,
    val startDate : Date?,
    val endDate: Date?,
    val scheduleName : String?,
    val wallpaperUrl : String?,
    val content : String?,
)
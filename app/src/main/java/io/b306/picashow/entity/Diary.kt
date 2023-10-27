package io.b306.picashow.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.b306.picashow.converter.Converters
import java.util.Date

@Entity(tableName = "diary")
@TypeConverters(Converters::class)
data class Diary (
    @PrimaryKey(autoGenerate = true) val diarySeq : Long? = 0L,
    val date : Date?,
    val title : String?,
    val content : String?,
    )
package io.b306.picashow.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import io.b306.picashow.converter.Converters
import java.util.Date

@Entity(tableName = "member")
@TypeConverters(Converters::class)
data class Member (
    @PrimaryKey(autoGenerate = true) val memberSeq : Long? = 0L,
    val fcmToken : String?,
    val theme : String?,
    val color : Char?,
    val keyword : String?,
    val birth : Date?,
    val gender : Int?,
    val hasPerson : Int?,
    )
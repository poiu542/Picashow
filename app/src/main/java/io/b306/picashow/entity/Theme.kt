package io.b306.picashow.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "theme")
data class Theme(
    @PrimaryKey(autoGenerate = true) val scheduleSeq : Long?,
    val keyWord : String?,

    )

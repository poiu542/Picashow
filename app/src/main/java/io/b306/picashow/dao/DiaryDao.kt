package io.b306.picashow.dao

import androidx.room.Dao
import androidx.room.Query
import io.b306.picashow.entity.Diary
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DiaryDao : BaseDao<Diary> {

    @Query("SELECT * FROM diary")
    fun getAll() : Flow<List<Diary>>

    @Query("SELECT * FROM diary WHERE date = :selectedDate")
    fun getDiaryByDate(selectedDate: Long): Flow<List<Diary>>


}
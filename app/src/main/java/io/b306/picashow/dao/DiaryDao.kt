package io.b306.picashow.dao

import androidx.room.Dao
import androidx.room.Query
import io.b306.picashow.entity.Diary
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao : BaseDao<Diary> {

    @Query("SELECT * FROM diary")
    fun getAll() : Flow<List<Diary>>
}
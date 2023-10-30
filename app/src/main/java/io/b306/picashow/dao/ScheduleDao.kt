package io.b306.picashow.dao

import androidx.room.Dao
import androidx.room.Query
import io.b306.picashow.entity.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao: BaseDao<Schedule> {
    @Query("SELECT * FROM schedule")
    fun getAll() : Flow<List<Schedule>>
}
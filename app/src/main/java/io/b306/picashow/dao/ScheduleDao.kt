package io.b306.picashow.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import io.b306.picashow.entity.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao: BaseDao<Schedule> {
    @Query("SELECT * FROM schedule")
    fun getAll() : Flow<List<Schedule>>

    @Query("SELECT * FROM schedule WHERE startDate >= :startTimestamp AND startDate < :endTimestamp")
    fun getSchedulesForDate(startTimestamp: Long, endTimestamp: Long): Flow<List<Schedule>>

    @Query("SELECT * FROM schedule WHERE scheduleSeq = :id")
    fun getScheduleById(id: String): Schedule?

    @Update
    override suspend fun update(schedule: Schedule)
}
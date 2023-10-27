package io.b306.picashow.repository

import androidx.annotation.WorkerThread
import io.b306.picashow.dao.MemberDao
import io.b306.picashow.dao.ScheduleDao
import io.b306.picashow.entity.Diary
import io.b306.picashow.entity.Member
import io.b306.picashow.entity.Schedule
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val scheduleDao: ScheduleDao) {
    val allSchedule: Flow<List<Schedule>> = scheduleDao.getAll()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(schedule: Schedule) {
        scheduleDao.insert(schedule)
    }
    
}
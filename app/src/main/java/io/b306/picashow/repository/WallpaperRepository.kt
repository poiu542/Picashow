package io.b306.picashow.repository

import androidx.annotation.WorkerThread
import io.b306.picashow.dao.MemberDao
import io.b306.picashow.dao.ScheduleDao
import io.b306.picashow.dao.WallpaperDao
import io.b306.picashow.entity.Diary
import io.b306.picashow.entity.Member
import io.b306.picashow.entity.Schedule
import io.b306.picashow.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

class WallpaperRepository(private val wallpaperDao: WallpaperDao) {
    val allSchedule: Flow<List<Wallpaper>> = wallpaperDao.getAll()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(wallpaper: Wallpaper ) {
        wallpaperDao.insert(wallpaper)
    }
    
}
package io.b101.picashow.repository

import androidx.annotation.WorkerThread
import io.b101.picashow.dao.MemberDao
import io.b101.picashow.dao.ScheduleDao
import io.b101.picashow.dao.WallpaperDao
import io.b101.picashow.entity.Diary
import io.b101.picashow.entity.Member
import io.b101.picashow.entity.Schedule
import io.b101.picashow.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

class WallpaperRepository(private val wallpaperDao: WallpaperDao) {
    val allSchedule: Flow<List<Wallpaper>> = wallpaperDao.getAll()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(wallpaper: Wallpaper ) {
        wallpaperDao.insert(wallpaper)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(wallpaper: Wallpaper) {
        wallpaperDao.delete(wallpaper)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(wallpaper: Wallpaper) {
        wallpaperDao.update(wallpaper)
    }

}
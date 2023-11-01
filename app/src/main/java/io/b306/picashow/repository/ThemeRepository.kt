package io.b306.picashow.repository

import androidx.annotation.WorkerThread
import io.b306.picashow.dao.DiaryDao
import io.b306.picashow.dao.ThemeDao
import io.b306.picashow.entity.Diary
import io.b306.picashow.entity.Theme
import kotlinx.coroutines.flow.Flow

class ThemeRepository(private val themeDao: ThemeDao) {
    val allTheme: Flow<List<Theme>> = themeDao.getAll()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(theme: Theme) {
        themeDao.insert(theme)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(theme: Theme) {
        themeDao.delete(theme)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(theme: Theme) {
        themeDao.update(theme)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        themeDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(themes: List<Theme>) {
        themeDao.insertAll(themes)
    }
}
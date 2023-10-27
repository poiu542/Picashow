package io.b306.picashow.repository

import androidx.annotation.WorkerThread
import io.b306.picashow.dao.DiaryDao
import io.b306.picashow.entity.Diary
import kotlinx.coroutines.flow.Flow

class DiaryRepository(private val diaryDao: DiaryDao) {
    val allDiarys: Flow<List<Diary>> = diaryDao.getAll()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(diary: Diary) {
        diaryDao.insert(diary)
    }

}
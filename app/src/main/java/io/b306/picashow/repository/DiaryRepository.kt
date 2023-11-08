package io.b306.picashow.repository

import androidx.annotation.WorkerThread
import io.b306.picashow.dao.DiaryDao
import io.b306.picashow.entity.Diary
import kotlinx.coroutines.flow.Flow
import java.util.Date

class DiaryRepository(private val diaryDao: DiaryDao) {
    val allDiarys: Flow<List<Diary>> = diaryDao.getAll()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(diary: Diary) {
        diaryDao.insert(diary)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(diary: Diary) {
        diaryDao.delete(diary)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(diary: Diary) {
        diaryDao.update(diary)
    }

    // DiaryRepository.kt에 새로운 함수 추가
    fun getDiaryByDate(selectedDate: Long): Flow<List<Diary>> {
        return diaryDao.getDiaryByDate(selectedDate)
    }

    // DiaryRepository 클래스에 추가
    suspend fun getDiary(diarySeq: Int): Diary? {
        return diaryDao.getDiary(diarySeq)
    }

}
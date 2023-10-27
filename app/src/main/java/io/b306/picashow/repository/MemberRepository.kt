package io.b306.picashow.repository

import androidx.annotation.WorkerThread
import io.b306.picashow.dao.MemberDao
import io.b306.picashow.entity.Diary
import io.b306.picashow.entity.Member
import kotlinx.coroutines.flow.Flow

class MemberRepository(private val memberDao: MemberDao) {
    val allDiarys: Flow<List<Member>> = memberDao.getAll()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(member: Member) {
        memberDao.insert(member)
    }
    
}
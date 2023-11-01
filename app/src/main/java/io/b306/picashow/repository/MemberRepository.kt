package io.b306.picashow.repository

import androidx.annotation.WorkerThread
import io.b306.picashow.dao.MemberDao
import io.b306.picashow.entity.Member
import kotlinx.coroutines.flow.Flow

class MemberRepository(private val memberDao: MemberDao) {
    val allMembers: Flow<List<Member>> = memberDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(member: Member) {
        memberDao.insert(member)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(member: Member) {
        memberDao.delete(member)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(member: Member) {
        memberDao.update(member)
    }

    fun getMember(memberSeq: Long): Flow<Member> {
        return memberDao.getMember(memberSeq)
    }
}

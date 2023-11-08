package io.b306.picashow

import android.app.Application
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.repository.MemberRepository
import io.b306.picashow.repository.ScheduleRepository

class PicaShowApp:Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { MemberRepository(database.memberDao()) }
    val scheduleRepository: ScheduleRepository by lazy { ScheduleRepository(database.scheduleDao()) }
}
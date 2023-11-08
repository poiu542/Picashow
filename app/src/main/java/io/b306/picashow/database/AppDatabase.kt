package io.b306.picashow.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.b306.picashow.dao.DiaryDao
import io.b306.picashow.dao.MemberDao
import io.b306.picashow.dao.ScheduleDao
import io.b306.picashow.dao.ThemeDao
import io.b306.picashow.dao.WallpaperDao
import io.b306.picashow.entity.Diary
import io.b306.picashow.entity.Member
import io.b306.picashow.entity.Schedule
import io.b306.picashow.entity.Theme
import io.b306.picashow.entity.Wallpaper

// UserDatabase represents database and contains the database holder and server the main access point for the underlying connection to your app's persisted, relational data.

@Database(
    entities = [Member::class, Diary::class, Schedule::class, Wallpaper::class, Theme::class],
    version = 21,                // <- Database version
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() { // <- Add 'abstract' keyword and extends RoomDatabase
    abstract fun memberDao() : MemberDao
    abstract fun diaryDao() : DiaryDao
    abstract fun scheduleDao() : ScheduleDao
    abstract fun wallPaperDao() : WallpaperDao
    abstract fun themeDao() : ThemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
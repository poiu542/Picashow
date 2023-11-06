package io.b306.picashow.dao

import androidx.room.Dao
import androidx.room.Query
import io.b306.picashow.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao: BaseDao<Wallpaper> {
    @Query("SELECT * FROM wallpaper")
    fun getAll() : Flow<List<Wallpaper>>
}
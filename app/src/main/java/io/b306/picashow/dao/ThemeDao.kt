package io.b306.picashow.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.b306.picashow.entity.Theme
import io.b306.picashow.entity.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao: BaseDao<Theme> {
    @Query("SELECT * FROM theme")
    fun getAll() : Flow<List<Theme>>
    @Query("DELETE FROM theme")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(themes: List<Theme>)
}
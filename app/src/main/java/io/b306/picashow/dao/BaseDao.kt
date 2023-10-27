package io.b306.picashow.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj : T)

    @Delete
    suspend fun delete(obj : T)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(obj : T)

}
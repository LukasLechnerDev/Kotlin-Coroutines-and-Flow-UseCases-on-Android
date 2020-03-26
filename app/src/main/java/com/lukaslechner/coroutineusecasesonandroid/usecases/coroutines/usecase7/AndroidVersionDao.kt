package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AndroidVersionDao {

    @Query("SELECT * FROM androidversions")
    suspend fun getAndroidVersions(): List<AndroidVersionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(androidVersionEntity: AndroidVersionEntity)

    @Query("DELETE FROM androidversions")
    suspend fun clear()
}
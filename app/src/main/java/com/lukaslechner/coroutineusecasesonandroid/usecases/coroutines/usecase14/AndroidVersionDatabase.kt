package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AndroidVersionEntity::class], version = 1, exportSchema = false)
abstract class AndroidVersionDatabase : RoomDatabase() {

    abstract fun androidVersionDao(): AndroidVersionDao

    companion object {
        private var INSTANCE: AndroidVersionDatabase? = null

        fun getInstance(context: Context): AndroidVersionDatabase {
            if (INSTANCE == null) {
                synchronized(AndroidVersionDatabase::class) {
                    INSTANCE = buildRoomDb(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDb(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AndroidVersionDatabase::class.java,
                "androidversions_usecase12.db"
            ).build()

    }

}


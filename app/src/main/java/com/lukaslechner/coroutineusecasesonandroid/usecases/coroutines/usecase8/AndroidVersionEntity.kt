package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion

@Entity(tableName = "androidversions")
data class AndroidVersionEntity(@PrimaryKey val apiLevel: Int, val name: String)

fun List<AndroidVersionEntity>.mapToUiModelList() = map {
    AndroidVersion(it.apiLevel, it.name)
}

fun AndroidVersion.mapToEntity() = AndroidVersionEntity(this.apiLevel, this.name)
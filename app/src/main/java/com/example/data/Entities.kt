package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModifiedAt: Long = System.currentTimeMillis(),
    val thumbnailPath: String? = null,
    val aspectRatio: Float = 16f / 9f
)

@Entity(tableName = "clips")
data class Clip(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val filePath: String,
    val type: ClipType,
    val startInMediaMs: Long,
    val endInMediaMs: Long,
    val startInTimelineMs: Long,
    val durationMs: Long,
    val layerIndex: Int = 0,
    val speed: Float = 1.0f,
    val volume: Float = 1.0f
)

enum class ClipType {
    VIDEO, AUDIO, IMAGE, TEXT
}

package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.EditorDatabase
import com.example.processing.VideoExporter
import com.example.repository.EditorRepository

object ManualDI {
    private var database: EditorDatabase? = null
    private var repository: EditorRepository? = null
    private var exporter: VideoExporter? = null

    fun initialize(context: Context) {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                EditorDatabase::class.java,
                "editor_database"
            ).fallbackToDestructiveMigration().build()
        }
    }

    fun getRepository(context: Context): EditorRepository {
        if (repository == null) {
            val db = database ?: Room.databaseBuilder(
                context.applicationContext,
                EditorDatabase::class.java,
                "editor_database"
            ).fallbackToDestructiveMigration().build().also { database = it }
            repository = EditorRepository(db.projectDao(), db.clipDao())
        }
        return repository!!
    }

    fun getExporter(context: Context): VideoExporter {
        if (exporter == null) {
            exporter = VideoExporter(context.applicationContext)
        }
        return exporter!!
    }
}

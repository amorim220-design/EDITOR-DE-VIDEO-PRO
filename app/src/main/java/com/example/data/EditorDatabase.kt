package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Project::class, Clip::class], version = 1, exportSchema = false)
abstract class EditorDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun clipDao(): ClipDao
}

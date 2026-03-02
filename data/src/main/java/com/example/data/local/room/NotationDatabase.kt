package com.example.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        NoteEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class NotationDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

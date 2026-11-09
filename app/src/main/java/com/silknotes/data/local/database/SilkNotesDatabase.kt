package com.silknotes.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.silknotes.data.local.database.converters.ColorConverter
import com.silknotes.data.local.database.converters.FolderIconConverter
import com.silknotes.data.local.database.converters.InstantConverter
import com.silknotes.data.local.database.converters.StrokeListConverter
import com.silknotes.data.local.database.converters.StrokePointListConverter

@Database(
    entities = [FolderEntity::class, NoteEntity::class, PageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InstantConverter::class,
    ColorConverter::class,
    FolderIconConverter::class,
    StrokeListConverter::class,
    StrokePointListConverter::class
)
abstract class SilkNotesDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun noteDao(): NoteDao
    abstract fun pageDao(): PageDao

    companion object {
        @Volatile private var INSTANCE: SilkNotesDatabase? = null

        fun getInstance(context: Context): SilkNotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SilkNotesDatabase::class.java,
                    "silknotes.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
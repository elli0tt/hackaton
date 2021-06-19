package com.example.hackatonapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hackatonapp.data.database.dao.PatientDao
import com.example.hackatonapp.data.database.dao.PatientNoteDao
import com.example.hackatonapp.data.database.entities.PatientEntity
import com.example.hackatonapp.data.database.entities.PatientNoteEntity

@Database(entities = [PatientEntity::class, PatientNoteEntity::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract val patientDao: PatientDao
    abstract val patientNoteDao: PatientNoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            if (INSTANCE == null) {
                synchronized(AppRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppRoomDatabase::class.java, "app_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
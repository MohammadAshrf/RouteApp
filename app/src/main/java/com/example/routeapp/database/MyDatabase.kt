package com.example.routeapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Courses::class], version = 12)
abstract class MyDatabase :RoomDatabase(){

    abstract fun getCoursesDao(): CoursesDao

    companion object{
        private var databaseInstance: MyDatabase? = null
        fun getInstance(context: Context): MyDatabase{
            if (databaseInstance == null)
                databaseInstance = Room.databaseBuilder(context,MyDatabase::class.java,"Route Courses DB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

            return databaseInstance!!
        }
    }

}
package com.example.routeapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CoursesDao {
    @Insert
    fun insertCourse(course: Course)

    @Query("SELECT * FROM Courses")
    fun getAllCourses(): List<Course>
}
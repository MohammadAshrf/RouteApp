package com.example.routeapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String,
    val description : String,

)

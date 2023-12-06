package com.example.routeapp.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.routeapp.database.Courses

class MainViewModel : ViewModel() {
    val courses = mutableStateOf(listOf<Courses>())
}

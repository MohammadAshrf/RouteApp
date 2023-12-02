package com.example.routeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.routeapp.database.Course
import com.example.routeapp.database.MyDatabase
import com.example.routeapp.ui.theme.RouteAppTheme

class AddCourseActivity : ComponentActivity() {
    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        //Unified Resource ID
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.e("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.e("PhotoPicker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RouteAppTheme {
                // A surface container using the 'background' color from the theme
                AddCourseContent(
                    openGalleryClick = {
                        pickMedia.launch(PickVisualMediaRequest())
                    },
                    onNavigationClick = { finish() },
                    onSaveCourseClick = {
                        finish()
                    }
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseContent(
    onNavigationClick: () -> Unit,
    openGalleryClick: () -> Unit,
    onSaveCourseClick: () -> Unit
) {

    Scaffold(
        topBar = {
            RouteTopAppBar(
                navigationIcon = R.drawable.ic_arrow_back,
                title = "Add New Course",
                navigationIconOnClickListener = { onNavigationClick() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it.calculateTopPadding())
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val title = remember {
                mutableStateOf("")
            }
            val titleError = remember {
                mutableStateOf("")
            }
            val description = remember {
                mutableStateOf("")
            }
            val descriptionError = remember {
                mutableStateOf("")
            }
            val context = LocalContext.current
            RouteTextFeild(lable = "Course Title", title)
            Spacer(modifier = Modifier.height(8.dp))
            RouteTextFeild("Course Description", description)
            Spacer(modifier = Modifier.height(8.dp))
            RouteButton(title = "Select Course Image", onButtonClick = openGalleryClick)
            Spacer(modifier = Modifier.height(8.dp))
            RouteButton(
                title = "Save Course",
                enabled = title.value.length > 2 && description.value.length > 2,
                onButtonClick = {
                    if (validateData(
                            title.value,
                            titleError,
                            description.value,
                            descriptionError
                        )
                    ) {
                        /**
                         * Call Room DataBase to save course
                         */
                        MyDatabase.getInstance(context).getCoursesDao()
                            .insertCourse(
                                Course(
                                    name = title.value,
                                    description = description.value
                                )
                            )
                        onSaveCourseClick()

                    } else {
                        Toast.makeText(context, titleError.value, Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

}

fun validateData(
    courseTitle: String,
    titleError: MutableState<String>,
    courseDescription: String,
    descriptionError: MutableState<String>
): Boolean {
    if (courseTitle.isEmpty() || courseTitle.isBlank() || courseTitle.length < 2) {
        titleError.value = "Title Invalid"
        return false
    } else {
        titleError.value = ""
    }

    if (courseDescription.isEmpty() || courseDescription.isBlank() || courseDescription.length < 2) {
        descriptionError.value = "Description Invalid"
        return false
    } else {
        descriptionError.value = ""
    }
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteTextFeild(lable: String, mutableState: MutableState<String>) {
    OutlinedTextField(
        value = mutableState.value,
        onValueChange = {
            mutableState.value = it
        },
        label = { Text(text = lable) }
    )
}

@Composable
fun RouteButton(
    title: String,
    onButtonClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = {
            onButtonClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorBlue)),
        enabled = enabled
    ) {
        Text(text = title)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    RouteAppTheme {
        AddCourseContent(onNavigationClick = {}, openGalleryClick = {}, onSaveCourseClick = {})
    }
}
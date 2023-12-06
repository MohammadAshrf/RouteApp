package com.example.routeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.imageLoader
import coil.request.ImageRequest
import com.example.routeapp.database.Courses
import com.example.routeapp.database.MyDatabase
import com.example.routeapp.ui.RouteTopAppBar
import com.example.routeapp.ui.theme.RouteAppTheme

class AddCourseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            RouteAppTheme {
                var imagePath by remember {
                    mutableStateOf("")
                }
                val request = ImageRequest.Builder(context)
                    .data(imagePath)
                    .build()


                val galleryLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = {
                        if (it != null) {
                            Log.e("PhotoPicker", "Selected URI: $it")
                            imagePath = it.toString()
                        } else {
                            Log.e("PhotoPicker", "No media selected")
                        }
                    }
                )

                // API Call open New Thread
                // suspend
                // A surface container using the 'background' color from the theme
                AddCourseContent(
                    openGalleryClick = {
                        galleryLauncher.launch(PickVisualMediaRequest())
                        // hiltViewModel()
                    },
                    onNavigationClick = { finish() },
                    onSaveCourseClick = {
                        setResult(RESULT_OK)
                        imageLoader.enqueue(request)
                        finish()
                    },
                    imagePath = imagePath
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
    onSaveCourseClick: () -> Unit,
    imagePath: String
) {

    Scaffold(
        topBar = {
            RouteTopAppBar(
                navigationIcon = R.drawable.ic_arrow_back,
                title = "Add New Courses",
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
            val picture = remember {
                mutableStateOf("")
            }
            picture.value = imagePath

            val context = LocalContext.current

            RouteTextField(label = "Courses Title", title)
            Spacer(modifier = Modifier.height(8.dp))

            RouteTextField("Courses Description", description)
            Spacer(modifier = Modifier.height(8.dp))

            RouteButton(title = "Select Courses Image", onButtonClick = openGalleryClick)
            Spacer(modifier = Modifier.height(8.dp))

            RouteButton(
                title = "Save Courses",
                enabled = title.value.length > 2 && description.value.length > 2,
                onButtonClick = {
                    /**
                     * Call Room DataBase to save course
                     */
                    MyDatabase.getInstance(context).getCoursesDao()
                        .insertCourse(
                            Courses(
                                name = title.value,
                                description = description.value,
                                picture = picture.value
                            )
                        )

                    onSaveCourseClick()


                })
        }
    }

}

//fun validateData(
//    courseTitle: String,
//    titleError: MutableState<String>,
//    courseDescription: String,
//    descriptionError: MutableState<String>
//): Boolean {
//    if (courseTitle.isEmpty() || courseTitle.isBlank() || courseTitle.length < 2) {
//        titleError.value = "Title Invalid"
//        return false
//    } else {
//        titleError.value = ""
//    }
//
//    if (courseDescription.isEmpty() || courseDescription.isBlank() || courseDescription.length < 2) {
//        descriptionError.value = "Description Invalid"
//        return false
//    } else {
//        descriptionError.value = ""
//    }
//    return true
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteTextField(label: String, mutableState: MutableState<String>) {
    OutlinedTextField(
        value = mutableState.value,
        onValueChange = {
            mutableState.value = it
        },
        label = { Text(text = label) },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.colorBlue),
            unfocusedLabelColor = Color.LightGray,
            focusedLabelColor = Color.Blue,
        )
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
        AddCourseContent(
            onNavigationClick = {},
            openGalleryClick = {},
            onSaveCourseClick = {},
            imagePath = ""

            // Glide - Coil
            // fetch local images
        )
    }
}
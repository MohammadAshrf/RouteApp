package com.example.routeapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.routeapp.AddCourseActivity
import com.example.routeapp.R
import com.example.routeapp.database.Courses
import com.example.routeapp.database.MyDatabase
import com.example.routeapp.ui.theme.RouteAppTheme

class MainActivity : ComponentActivity() {
//    val addCourseLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == RESULT_OK) {
//
//            }
//
//        }
//    }
//
//    private fun fetchCoursesFromDB(callback: (List<Courses>) -> Unit) {
//        val courses = MyDatabase.getInstance(this).getCoursesDao().getAllCourses()
//        callback(courses)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RouteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    var newCourses by remember {
                        mutableStateOf(listOf<Courses>())
                    }
                    newCourses = MyDatabase.getInstance(context).getCoursesDao().getAllCourses()

                    val activityLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult(),
                        onResult = {
                            if (it.resultCode == RESULT_OK) {
                                newCourses =
                                    MyDatabase.getInstance(context).getCoursesDao().getAllCourses()
                            }
                        }
                    )

                    MainContent(
                        newCourses,
                        callback = {
                            val intent = Intent(this, AddCourseActivity::class.java)
                            activityLauncher.launch(intent)
                            //startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    newCourses: List<Courses>,
    callback: () -> Unit,
) {
    Scaffold(
        topBar = {
            RouteTopAppBar(navigationIcon = null, {}, title = "Route App")
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = 8.dp, bottom = 16.dp),
                onClick = {
                    callback()
//                    lazyListState()
                },
                containerColor = colorResource(id = R.color.colorBlue),
                contentColor = colorResource(id = R.color.white)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add Icon"
                )
            }
        },
        //Exo Player

        floatingActionButtonPosition = FabPosition.End
    ) {


        // Fetch initial data from the database

        LazyColumn(

            modifier = Modifier.padding(
                top = it.calculateTopPadding()
            )
        ) {
            items(newCourses.size) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp, horizontal = 16.dp
                        ), shape = CardDefaults.outlinedShape
                ) {
                    val item = newCourses[it]

                    AsyncImage(
                        model = Uri.parse(item.picture),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(150.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        contentScale = ContentScale.FillBounds,
                    )

                    Text(
                        text = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = item.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )


                }
            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    RouteAppTheme {
        MainContent(listOf()) {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteTopAppBar(
    navigationIcon: Int? = null,
    navigationIconOnClickListener: () -> Unit,
    title: String
) {
    TopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = colorResource(id = R.color.colorBlue),
            titleContentColor = colorResource(id = R.color.white),
            navigationIconContentColor = colorResource(id = R.color.white)
        ),
        navigationIcon = {
            if (navigationIcon != null)
                IconButton(onClick = {
                    navigationIconOnClickListener()
                })
                {
                    Icon(
                        painter = painterResource(id = navigationIcon),
                        contentDescription = "back icon"
                    )
                }
        }
    )
}
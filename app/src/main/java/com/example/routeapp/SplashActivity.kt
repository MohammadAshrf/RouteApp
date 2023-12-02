package com.example.routeapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.routeapp.ui.theme.RouteAppTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RouteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Handler(Looper.getMainLooper()).postDelayed(
                        { val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                            finish()
                        },
                        1000
                    )
                    SplashContent()
                }
            }
        }
    }
}

@Composable
fun SplashContent() {
    Image(
        painter = painterResource(id = R.drawable.splash_screen),
        contentDescription = "Splash Picture",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    RouteAppTheme {
        SplashContent()
    }
}
package io.b306.picashow

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import io.b306.picashow.ui.page.AddSchedulePage
import io.b306.picashow.ui.theme.PicaShowTheme
import io.b306.picashow.ui.theme.Pink80
import io.b306.picashow.ui.theme.PurpleGrey80

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background) {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    PicaShowTheme {
//        Greeting("Android")
//    }
//}


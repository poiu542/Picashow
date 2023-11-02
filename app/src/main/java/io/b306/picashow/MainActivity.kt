package io.b306.picashow

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.repository.ScheduleRepository
import io.b306.picashow.viewmodel.ScheduleViewModel
import io.b306.picashow.viewmodel.ScheduleViewModelFactory

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
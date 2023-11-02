package io.b306.picashow

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.repository.MemberRepository
import io.b306.picashow.viewmodel.MemberViewModel
import io.b306.picashow.viewmodel.MemberViewModelFactory
import io.b306.picashow.viewmodel._myInfo
import io.b306.picashow.viewmodel.please

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background) {
                val application = applicationContext as PicaShowApp
                val memberViewModelFactory = MemberViewModelFactory(application.repository)
                val memberViewModel = viewModel<MemberViewModel>(
                    factory = memberViewModelFactory
                )
                memberViewModel.getMember(1L)
                val navController = rememberNavController()
                if(please.value)MainScreen(navController = navController)
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


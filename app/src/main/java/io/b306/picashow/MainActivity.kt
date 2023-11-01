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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background) {

                val navController = rememberNavController()
                MainScreen(navController = navController)

                val context = LocalContext.current
                val memberDao = AppDatabase.getDatabase(context).memberDao()
                val memberRepository = MemberRepository(memberDao)
                val memberViewModelFactory = MemberViewModelFactory(memberRepository)

                val memberViewModel = viewModel<MemberViewModel>(
                    factory = memberViewModelFactory
                )

                val member = memberViewModel.getMember(1L)
                // Access isTutorial property from the retrieved Member object
                Log.d("member = {}", _myInfo.value?.isTutorial.toString())
//                val isTutorial = member?. ?: null
                // Use the value as needed
//                Log.d("sex",isTutorial.toString())
                _myInfo.value?.isTutorial
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


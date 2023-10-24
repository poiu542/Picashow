package io.b306.picashow

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.b306.picashow.ui.components.BottomNavigation
import io.b306.picashow.ui.components.BottomNavigationItem
import io.b306.picashow.ui.page.MainPage

@Composable
fun MainScreen(navController: NavHostController) {
    var title by remember { mutableStateOf("Schedule") }

    val updatedNavController = rememberUpdatedState(navController)

    // NavController의 back stack entry를 관찰
    LaunchedEffect(updatedNavController.value) {
        updatedNavController.value.addOnDestinationChangedListener { _, destination, _ ->
            title = when(destination.route) {
                "firstPage" -> "Calendar"
                "secondPage" -> "Main Page"
                "thirdPage" -> "Diary"
                else -> "Schedule"
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(title)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(navController = navController, startDestination = "firstPage") {
                composable("firstPage") { FirstPage() }
                composable("secondPage") { SecondPage() }
                composable("thirdPage") { ThirdPage() }
            }

            val bottomNavItems = listOf(
                BottomNavigationItem(
                    icon = { Image(painter = painterResource(id = R.drawable.calender), contentDescription = null) },
                    selected = navController.currentDestination?.route == "firstPage",
                    onClick = { navController.navigate("firstPage") }
                ),
                BottomNavigationItem(
                    icon = { Image(painter = painterResource(id = R.drawable.today), contentDescription = null) },
                    selected = navController.currentDestination?.route == "secondPage",
                    onClick = { navController.navigate("secondPage") }
                ),
                BottomNavigationItem(
                    icon = { Image(painter = painterResource(id = R.drawable.paintdiary), contentDescription = null) },
                    selected = navController.currentDestination?.route == "thirdPage",
                    onClick = { navController.navigate("thirdPage") }
                )
            )

            BottomNavigation(
                modifier = Modifier.align(Alignment.BottomCenter),
                items = bottomNavItems
            )
        }
    }
}

@Composable
fun FirstPage() {
    // TODO 첫번째 캘린더 - 두현이 페이지
}

@Composable
fun SecondPage() {
    // TODO 두번째 사실상 메인 페이지 - 할일 페이지임
    MainPage()
}

@Composable
fun ThirdPage() {
    // TODO 세 번째 귀태귀 그림 일기 페이지
}



@Composable
fun TopAppBar(title : String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(Icons.Default.List, contentDescription = null)
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

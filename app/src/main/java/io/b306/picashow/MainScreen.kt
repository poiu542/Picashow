package io.b306.picashow

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.b306.picashow.ui.components.BottomNavigation
import io.b306.picashow.ui.components.BottomNavigationItem
import io.b306.picashow.ui.components.TopAppBar
import io.b306.picashow.ui.page.AddSchedulePage
import io.b306.picashow.ui.page.DiaryPage
import io.b306.picashow.ui.page.MainPage
import io.b306.picashow.ui.theme.MainBackground
import io.b306.picashow.ui.page.firstPage
import io.b306.picashow.ui.page.tutorialPage

@Composable
fun MainScreen(navController: NavHostController) {
    var title by remember { mutableStateOf("") }

    var showAppBarAndNavBar by remember { mutableStateOf(true) }  // 상태 변수 추가
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
            showAppBarAndNavBar = destination.route != "addSchedulePage"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackground)  // 전체 배경색을 설정합니다.
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // showAppBarAndNavBar의 값에 따라 TopAppBar 표시
            if (showAppBarAndNavBar) {
                TopAppBar(
                    title = title,
                    onAddClick = {
                        if(navController.currentDestination?.route == "secondPage") {
                            navController.navigate("addSchedulePage")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                NavHost(navController = navController, startDestination = "secondPage") {
                    composable("firstPage") { FirstPage() }
                    composable("secondPage") { SecondPage() }
                    composable("thirdPage") { ThirdPage() }
                    composable("addSchedulePage") { AddSchedulePage() }
                }
            }
                val bottomNavItems = listOf(
                    BottomNavigationItem(
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.calender),
                                contentDescription = null
                            )
                        },
                        selected = navController.currentDestination?.route == "firstPage",
                        onClick = { navController.navigate("firstPage") }
                    ),
                    BottomNavigationItem(
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.today),
                                contentDescription = null
                            )
                        },
                        selected = navController.currentDestination?.route == "secondPage" || navController.previousBackStackEntry == null,
                        onClick = { navController.navigate("secondPage") }
                    ),
                    BottomNavigationItem(
                        icon = {
                            Image(
                                painter = painterResource(id = R.drawable.paintdiary),
                                contentDescription = null
                            )
                        },
                        selected = navController.currentDestination?.route == "thirdPage",
                        onClick = { navController.navigate("thirdPage") }
                    )
                )
            if (showAppBarAndNavBar) {
                BottomNavigation(
                    items = bottomNavItems
                )
            }
        }
    }
}

@Composable
fun FirstPage() {
    tutorialPage()
}

@Composable
fun SecondPage() {
    // TODO 두번째 사실상 메인 페이지 - 할일 페이지임
    MainPage()
}

@Composable
fun ThirdPage() {
    DiaryPage()
}
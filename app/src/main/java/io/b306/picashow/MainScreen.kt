package io.b306.picashow

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.repository.MemberRepository
import io.b306.picashow.ui.components.BottomNavigation
import io.b306.picashow.ui.components.BottomNavigationItem
import io.b306.picashow.ui.components.TopAppBar
import io.b306.picashow.ui.page.AddSchedulePage
import io.b306.picashow.ui.page.DiaryPage
import io.b306.picashow.ui.page.MainPage
import io.b306.picashow.ui.theme.MainBackground
import io.b306.picashow.ui.page.firstPage
import io.b306.picashow.ui.page.tutorialPage
import io.b306.picashow.viewmodel.MemberViewModel
import io.b306.picashow.viewmodel.MemberViewModelFactory
import io.b306.picashow.viewmodel._myInfo
import kotlinx.coroutines.CoroutineScope

var flag = mutableStateOf(false);
@Composable
fun MainScreen(navController: NavHostController) {
    var title by remember { mutableStateOf("") }

    var showAppBarAndNavBar by remember { mutableStateOf(true) }  // 상태 변수 추가
    val updatedNavController = rememberUpdatedState(navController)

    val context = LocalContext.current
    val memberDao = AppDatabase.getDatabase(context).memberDao()
    val memberRepository = MemberRepository(memberDao)
    val memberViewModelFactory = MemberViewModelFactory(memberRepository)

    val memberViewModel = viewModel<MemberViewModel>(
        factory = memberViewModelFactory
    )

    // NavController의 back stack entry를 관찰
    val block: suspend CoroutineScope.() -> Unit = {
        updatedNavController.value.addOnDestinationChangedListener { _, destination, _ ->
            title = when (destination.route) {
                "firstPage" -> "Calendar"
                "secondPage" -> "Main Page"
                "thirdPage" -> "Diary"
                else -> "Schedule"
            }
            showAppBarAndNavBar = destination.route != "addSchedulePage"

            // Observe changes in myInfo LiveData
//            val member = memberViewModel.myInfo.value
            // Access isTutorial property from the retrieved Member object
//            val isTutorial = member?.isTutorial ?: false
            // Use the value as needed
//            Log.d("sex",isTutorial.toString())
//            println("Is Tutorial: $isTutorial")
        }
    }
    LaunchedEffect(updatedNavController.value, block)

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
            flag.value = _myInfo.value?.isTutorial!!
            // showAppBarAndNavBar의 값에 따라 TopAppBar 표시
            if (showAppBarAndNavBar && flag.value) {
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
                val startDestination = if (!flag.value) "tutorialPage" else "secondPage"
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("firstPage") { FirstPage() }
                    composable("secondPage") { SecondPage() }
                    composable("thirdPage") { ThirdPage() }
                    composable("addSchedulePage") { AddSchedulePage(navController) }
                    composable("tutorialPage") { tutorialPage() }
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
            if (showAppBarAndNavBar && flag.value) {
                BottomNavigation(
                    items = bottomNavItems
                )
            }
        }
    }
}

@Composable
fun FirstPage() {
    firstPage()
}

@Composable
fun SecondPage() {
//    // TODO 두번째 사실상 메인 페이지 - 할일 페이지임
//
//    val scheduleRepository = remember { ScheduleRepository(scheduleDao = ) }
//    val viewModelFactory = ScheduleViewModelFactory(scheduleRepository)
//    val viewModel: ScheduleViewModel = viewModel(factory = viewModelFactory)

    MainPage()
}

@Composable
fun ThirdPage() {
    DiaryPage()
}
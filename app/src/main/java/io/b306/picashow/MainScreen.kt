package io.b306.picashow

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainScreen(navController: NavHostController) {
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

@Composable
fun FirstPage() {
    // TODO 첫번째 캘린더 - 두현이 페이지
}

@Composable
fun SecondPage() {
    // TODO 두번째 사실상 메인 페이지 - 할일 페이지임
}

@Composable
fun ThirdPage() {
    // TODO 세 번째 귀태귀 그림 일기 페이지
}



@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = null)
        Text(text = "Schedule", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun Calendar() {
    Text(text = "November", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
            DayItem(day = it)
        }
    }
}

@Composable
fun DayItem(day: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = day)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "1", fontWeight = FontWeight.Bold)  // 예시로 1을 표시하였습니다.
    }
}

@Composable
fun Tasks() {
    Text(text = "Today's Tasks", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))
    TaskItem(taskName = "User Interviews", time = "16:00 - 18:30")
    Spacer(modifier = Modifier.height(8.dp))
    // Add tasks items here...
}

@Composable
fun BottomNavigation(modifier: Modifier = Modifier, items: List<BottomNavigationItem>) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEach { item ->
            BottomNavigationItemComposable(
                icon = item.icon,
                selected = item.selected,
                onClick = item.onClick
            )
        }
    }
}

@Composable
fun BottomNavigationItemComposable(
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        icon()
        if (selected) {
            // 선택된 아이템을 시각적으로 표시하기 위한 로직 (예: 아이콘의 색 변경)
        }
    }
}

@Composable
fun TaskItem(taskName: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = taskName, modifier = Modifier.padding(8.dp))
        Text(text = time, modifier = Modifier.padding(8.dp))
    }
}
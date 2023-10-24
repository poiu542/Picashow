package io.b306.picashow.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.YearMonth
import java.util.Calendar

class MainPageUI {
}

@Composable
fun MainPage() {
    val today = Calendar.getInstance()
    val currentYear = today.get(Calendar.YEAR)
    val currentMonth = today.get(Calendar.MONTH) + 1 // Java의 Calendar MONTH는 0부터 시작합니다.
    val currentDay = today.get(Calendar.DAY_OF_MONTH)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Calendar(currentYear, currentMonth, currentDay)
        Spacer(modifier = Modifier.height(16.dp))
        Tasks()
    }
}

@Composable
fun Calendar(year: Int, month: Int, startDay: Int) {
    val daysInMonth = daysInMonth(year, month)
    val daysToShow = mutableListOf<Int?>()

    for (i in 0 until 7) {
        val currentDay = startDay + i
        if (currentDay <= daysInMonth) {
            daysToShow.add(currentDay)
        } else {
            daysToShow.add(currentDay - daysInMonth)  // 다음 달의 날짜로 업데이트
        }
    }
    val dayNames = getWeekDayNamesBasedOnStartDay(year, month, startDay)

    Text(text = monthToName(month), fontWeight = FontWeight.Bold, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))

    // 요일 이름 표시
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dayNames.forEach { dayName ->
            Text(text = dayName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
    Spacer(modifier = Modifier.height(4.dp))

    // 날짜 표시
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysToShow.forEach { day ->
            DayItem(day = day)
        }
    }
}

@Composable
fun DayItem(day: Int?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = day?.toString() ?: "", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

fun getWeekDayNamesBasedOnStartDay(year: Int, month: Int, startDay: Int): List<String> {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, startDay)  // month - 1 because Java Calendar's months start from 0

    val dayNames = mutableListOf<String>()

    for (i in 0..6) {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        dayNames.add(
            when (dayOfWeek) {
                Calendar.MONDAY -> "Mon"
                Calendar.TUESDAY -> "Tue"
                Calendar.WEDNESDAY -> "Wed"
                Calendar.THURSDAY -> "Thu"
                Calendar.FRIDAY -> "Fri"
                Calendar.SATURDAY -> "Sat"
                Calendar.SUNDAY -> "Sun"
                else -> ""
            }
        )
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return dayNames
}

fun daysInMonth(year: Int, month: Int): Int {
    // API 레벨 26부터 사용 가능
    val yearMonth = YearMonth.of(year, month)
    return yearMonth.lengthOfMonth()
}

fun monthToName(month: Int): String {
    return when(month) {
        1 -> "January"
        2 -> "February"
        3 -> "March"
        4 -> "April"
        5 -> "May"
        6 -> "June"
        7 -> "July"
        8 -> "August"
        9 -> "September"
        10 -> "October"
        11 -> "November"
        12 -> "December"
        else -> throw IllegalArgumentException("Invalid month: $month")
    }
}

@Composable
fun Tasks() {
    Text(text = "Today's Tasks", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))
    TaskItem(taskName = "User Interviews", time = "16:00 - 18:30")
    Spacer(modifier = Modifier.height(8.dp))
    TaskItem(taskName = "User Interviews", time = "16:00 - 18:30")
    Spacer(modifier = Modifier.height(8.dp))
    TaskItem(taskName = "User Interviews", time = "16:00 - 18:30")
    Spacer(modifier = Modifier.height(8.dp))
    TaskItem(taskName = "User Interviews", time = "16:00 - 18:30")
    Spacer(modifier = Modifier.height(8.dp))
    // Add tasks items here...
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

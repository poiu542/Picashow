package io.b306.picashow.ui.page

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.b306.picashow.ui.components.ShowDatePicker
import io.b306.picashow.ui.components.ShowTimePicker
import io.b306.picashow.ui.theme.NoneSelectBackground
import java.time.YearMonth
import java.util.Calendar

//class MainPageUI {
//}

@Composable
fun MainPage() {
    val today = Calendar.getInstance()
    val currentYear = today.get(Calendar.YEAR)
    val currentMonth = today.get(Calendar.MONTH) + 1 // Java의 Calendar MONTH는 0부터 시작합니다.
    val currentDay = today.get(Calendar.DAY_OF_MONTH)

    // 선택된 날짜를 관리하는 상태
    var selectedDay by remember { mutableIntStateOf(currentDay) }
    var selectedMonth by remember { mutableIntStateOf(currentMonth) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Calendar(currentYear, currentMonth, currentDay, selectedDay) { newSelectedDay, selectedMonthLocal ->
            selectedDay = newSelectedDay
            selectedMonth = if(selectedDay <= currentDay) {
                selectedMonthLocal + 1
            } else {
                selectedMonthLocal
            }
            Log.e("SelectedDay", "Selected day is: $selectedMonth/$selectedDay")  // 로그로 선택한 날짜 출력

            // TODO selectedMonth, Day를 사용해서 그 날짜에 해당하는 Task를 Room에서 불러오면 됨(코루틴 비둘기 써서)

        }
        Spacer(modifier = Modifier.height(16.dp))
        Tasks()
    }
}

@Composable
fun Calendar(year: Int, month: Int, startDay: Int, selectedDay: Int, onDaySelected: (Int, Int) -> Unit) {
    val daysInMonth = daysInMonth(year, month)
    val daysToShow = mutableListOf<Int?>()

    for (i in 0 until 30) {
        val currentDay = startDay + i
        if (currentDay <= daysInMonth) {
            daysToShow.add(currentDay)
        } else {
            daysToShow.add(currentDay - daysInMonth)  // 다음 달의 날짜로 업데이트
        }
    }
    val dayNames = getWeekDayNamesBasedOnStartDay(year, month, startDay)

    Text(text = monthToName(month), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
    Spacer(modifier = Modifier.height(8.dp))
    ShowTimePicker()
    ShowDatePicker()

    val lazyListState = rememberLazyListState()

    LazyRow(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(daysToShow) { index, day ->
            DayWithBackground(day, dayNames[index], day == selectedDay) {
                day?.let {
                    onDaySelected(it, month)
                }
            }
        }
    }
}

@Composable
fun DayWithBackground(day: Int?, dayName: String, isSelected: Boolean, onDayClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color.White else NoneSelectBackground
    val textColor = if (isSelected) Color.Black else Color.White

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(backgroundColor)
            .clickable(onClick = onDayClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = dayName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = textColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = day?.toString() ?: "", fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

fun getWeekDayNamesBasedOnStartDay(year: Int, month: Int, startDay: Int): List<String> {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, startDay)

    val dayNames = mutableListOf<String>()
    val repeatCount = 30 // 30번 반복

    for (j in 0 until repeatCount) {
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
        calendar.add(Calendar.DAY_OF_MONTH, 1) // 다음 날짜로 이동
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
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Text(
                text = "Today's Tasks",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(5) { // 5개의 TaskItem을 렌더링. 필요한 개수에 따라 변경 가능
            TaskItem(taskName = "기택아 Room 빨리 만들어줘", time = "16:00 - 18:30")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TaskItem(taskName: String, time: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF00A7A7), Color(0xFF2C2C2C)),
                    startY = 0f,
                    endY = 8f
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column {
            Text(
                text = taskName,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = time,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
                color = Color.White
            )
        }
    }
}

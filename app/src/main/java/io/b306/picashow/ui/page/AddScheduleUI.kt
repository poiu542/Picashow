package io.b306.picashow.ui.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.b306.picashow.ui.theme.PlaceDefault
import io.b306.picashow.ui.theme.TextFieldCursor
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun AddSchedulePage() {
    val selectedDate = remember { mutableStateOf(LocalDateTime.now()) }
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }
    val schedule = remember { mutableStateOf("") }
    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 16.dp, 16.dp, 80.dp) // 하단 padding 추가
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = schedule.value,
                onValueChange = { schedule.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "제목",
                        color = PlaceDefault // 텍스트의 색상을 지정
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White, // 텍스트 색상 설정
                    cursorColor = Color.White, // 커서 색상 설정
                    focusedIndicatorColor = TextFieldCursor // 마우스 포커스 시 보라색 밑줄을 제거합니다.
                )
            )

            Divider(
                color = Color.Gray, // Divider의 색상을 지정
                thickness = 1.dp,    // Divider의 두께를 지정
                modifier = Modifier.fillMaxWidth() // Divider의 너비를 TextField와 같게 설정
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    color = Color.White,
                    text = "${selectedDate.value.monthValue}월 " +
                            "${selectedDate.value.dayOfMonth}일 " +
                            "(${getDayOfWeek(selectedDate.value.dayOfWeek)})"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(color = Color.White, text = "→")
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    color = Color.White,
                    text = "${selectedDate.value.monthValue}월 " +
                            "${selectedDate.value.dayOfMonth}일 " +
                            "(${getDayOfWeek(selectedDate.value.dayOfWeek)})"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                color = Color.Gray, // Divider의 색상을 지정
                thickness = 1.dp,    // Divider의 두께를 지정
                modifier = Modifier.fillMaxWidth() // Divider의 너비를 TextField와 같게 설정
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 시간 설정 UI
            TimePicker(selectedTime.value) {
                selectedTime.value = it
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                color = Color.Gray, // Divider의 색상을 지정
                thickness = 1.dp,    // Divider의 두께를 지정
                modifier = Modifier.fillMaxWidth() // Divider의 너비를 TextField와 같게 설정
            )

            // 내용 정보 입력 필드
            TextField(
                value = content.value,
                onValueChange = { content.value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "메모",
                        color = PlaceDefault // 텍스트의 색상을 지정
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White, // 텍스트 색상 설정
                    cursorColor = Color.White, // 커서 색상 설정
                    focusedIndicatorColor = TextFieldCursor // 마우스 포커스 시 보라색 밑줄을 제거합니다.
                )
            )

            Divider(
                color = Color.Gray, // Divider의 색상을 지정
                thickness = 1.dp,    // Divider의 두께를 지정
                modifier = Modifier.fillMaxWidth() // Divider의 너비를 TextField와 같게 설정
            )

            Spacer(modifier = Modifier.height(32.dp))

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // 하단 중앙에 위치
        ) {
            Button(
                onClick = { /* TODO: 취소 로직 추가 */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("취소", fontSize = 20.sp)
            }

            Button(
                onClick = { /* TODO: 저장 로직 추가 */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("저장", fontSize = 20.sp)
            }
        }

    }
}

@Composable
fun TimePicker(time: LocalTime, onTimeSelected: (LocalTime) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "${time.hour}시", color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${time.minute}분", color = Color.White)
            // TODO: 시간 선택 로직 추가
        }
    }

}

fun getDayOfWeek(day: DayOfWeek): String {
    return when (day) {
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
    }
}
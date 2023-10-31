package io.b306.picashow.ui.page

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.b306.picashow.ui.theme.PlaceDefault
import io.b306.picashow.ui.theme.TextFieldCursor
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import io.b306.picashow.ui.components.CustomTimePicker
import io.b306.picashow.ui.components.GrayDivider

@Composable
fun AddSchedulePage() {
    val selectedStartDate = remember { mutableStateOf(LocalDateTime.now()) }
    val selectedEndDate = remember { mutableStateOf(LocalDateTime.now()) }
    val schedule = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    val selectedStartHour = remember { mutableIntStateOf(LocalDateTime.now().hour) }
    val selectedStartMinute = remember { mutableIntStateOf(LocalDateTime.now().minute) }
    val selectedEndHour = remember { mutableIntStateOf(LocalDateTime.now().hour) }
    val selectedEndMinute = remember { mutableIntStateOf(LocalDateTime.now().minute) }

    val context = LocalContext.current

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
                        color = PlaceDefault, // 텍스트의 색상을 지정
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White, // 텍스트 색상 설정
                    cursorColor = Color.White, // 커서 색상 설정
                    focusedIndicatorColor = TextFieldCursor // 마우스 포커스 시 보라색 밑줄을 제거합니다.
                )
            )

            GrayDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.clickable(
                        onClick = {
                            // 클릭 시 수행할 작업
                            showDatePicker(context) { year, month, day ->
                                val pickedDate = LocalDateTime.of(year, month, day, 0, 0)

                                if (pickedDate.isAfter(selectedEndDate.value)) {
                                    selectedEndDate.value = pickedDate
                                }

                                selectedStartDate.value = pickedDate
                            }
                        }
                    )
                ) {
                    Text(
                        color = Color.White,
                        text = "${selectedStartDate.value.monthValue}월 " +
                                "${selectedStartDate.value.dayOfMonth}일 " +
                                "(${getDayOfWeek(selectedStartDate.value.dayOfWeek)})",
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(color = Color.White, text = "→", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier.clickable(
                        onClick = {
                            // 클릭 시 수행할 작업
                            showDatePicker(context) { year, month, day ->
                                val pickedDate = LocalDateTime.of(year, month, day, 0, 0)
                                if (pickedDate.isBefore(selectedStartDate.value)) {
                                    selectedEndDate.value = selectedStartDate.value
                                } else {
                                    selectedEndDate.value = pickedDate
                                }
                            }
                        }
                    )
                ) {
                    Text(
                        color = Color.White,
                        text = "${selectedEndDate.value.monthValue}월 " +
                                "${selectedEndDate.value.dayOfMonth}일 " +
                                "(${getDayOfWeek(selectedEndDate.value.dayOfWeek)})",
                        fontSize = 21.sp
                    )
                }
            }

//            Spacer(modifier = Modifier.height(16.dp))
//
//            GrayDivider()
//
//            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // TimePickerBox와 CustomTimePicker를 포함하는 컨테이너
                Box(
                    modifier = Modifier.clickable(
                        onClick = {
                            // Box를 클릭하면 CustomTimePicker의 표시 여부를 토글합니다.
                            showTimePicker = !showTimePicker
                        }
                    )
                ) {
                    Text(
                        text = "${selectedStartHour.value.toString().padStart(2, '0')} : " +
                                selectedStartMinute.value.toString().padStart(2, '0'),
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                Text(color = Color.White, text = "→", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier.clickable(
                        onClick = {
                            // Box를 클릭하면 CustomTimePicker의 표시 여부를 토글합니다.
                            showTimePicker = !showTimePicker
                        }
                    )
                ) {
                    Text(
                        text = "${selectedEndHour.value.toString().padStart(2, '0')} : " +
                                selectedEndMinute.value.toString().padStart(2, '0'),
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                }
            }

            // 조건부로 CustomTimePicker를 렌더링합니다.
            if (showTimePicker) {
                Spacer(modifier = Modifier.height(16.dp))
                GrayDivider()
                Spacer(modifier = Modifier.height(16.dp))
                CustomTimePicker(selectedHour = selectedStartHour, selectedMinute = selectedStartMinute)
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            GrayDivider()

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

            GrayDivider()

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

fun showDatePicker(context: Context, dateSetListener: (Int, Int, Int) -> Unit) {
    val currentDateTime = LocalDateTime.now()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateSetListener(year, month + 1, dayOfMonth)
        },
        currentDateTime.year,
        currentDateTime.monthValue - 1,
        currentDateTime.dayOfMonth
    )
    datePickerDialog.show()
}
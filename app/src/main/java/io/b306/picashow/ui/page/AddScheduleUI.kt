package io.b306.picashow.ui.page

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import io.b306.picashow.UpdateImageService
import io.b306.picashow.api.ApiObject
import io.b306.picashow.api.image.CreateImageRequest
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.entity.Schedule
import io.b306.picashow.repository.ScheduleRepository
import io.b306.picashow.scheduleWallpaperChange
import io.b306.picashow.ui.components.CustomAlertDialog
import io.b306.picashow.ui.components.CustomTimePicker
import io.b306.picashow.ui.components.GrayDivider
import io.b306.picashow.ui.theme.PlaceDefault
import io.b306.picashow.ui.theme.TextFieldCursor
import io.b306.picashow.util.await
import io.b306.picashow.viewmodel.ScheduleViewModel
import io.b306.picashow.viewmodel.ScheduleViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
enum class TimePickerType {
    START, END
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun AddSchedulePage(navController : NavController) {

    // 의존성 주입
    val context = LocalContext.current

    val scheduleDao = AppDatabase.getDatabase(context).scheduleDao()
    val scheduleRepository = ScheduleRepository(scheduleDao)
    val scheduleViewModelFactory = ScheduleViewModelFactory(scheduleRepository)
    val scheduleViewModel = viewModel<ScheduleViewModel>(
        factory = scheduleViewModelFactory
    )

    val selectedStartDate = remember { mutableStateOf(LocalDateTime.now()) }
    val selectedStartHour = remember { mutableIntStateOf(LocalDateTime.now().hour) }
    val selectedStartMinute = remember { mutableIntStateOf(LocalDateTime.now().minute) }
    val selectedEndHour = remember { mutableIntStateOf(LocalDateTime.now().hour) }
    val selectedEndMinute = remember { mutableIntStateOf(LocalDateTime.now().minute) }
    val selectedEndDate = remember { mutableStateOf(LocalDateTime.now()) }
    val scheduleName = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    // 시간 선택기 상태를 관리할 MutableState를 정의
    var showingTimePicker = remember { mutableStateOf<TimePickerType?>(null) }

    // AlertDialog 상태
    var showDialogTitle by remember { mutableStateOf(false) }
    var showDialogDate by remember { mutableStateOf(false) }

    // 이미지 생성 후 받아올 Seq
    var scheduleSeq by remember { mutableStateOf<Long?>(null) }
    var hasScheduleBeenSaved by remember { mutableStateOf(false) }

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
                value = scheduleName.value,
                onValueChange = { scheduleName.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp),
                placeholder = {
                    Text(
                        text = "Title",
                        color = PlaceDefault, // 텍스트의 색상을 지정
                        fontSize = 18.sp
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White, // 텍스트 색상 설정
                    cursorColor = Color.White, // 커서 색상 설정
                    focusedIndicatorColor = TextFieldCursor // 마우스 포커스 시 보라색 밑줄을 제거합니다.
                ),
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
                        text = "${selectedStartDate.value.monthValue} / " +
                                "${selectedStartDate.value.dayOfMonth} " +
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
                        text = "${selectedEndDate.value.monthValue} / " +
                                "${selectedEndDate.value.dayOfMonth} " +
                                "(${getDayOfWeek(selectedEndDate.value.dayOfWeek)})",
                        fontSize = 21.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                            showingTimePicker.value = TimePickerType.START
                        }
                    )
                ) {
                    Text(
                        text = "${selectedStartHour.intValue.toString().padStart(2, '0')} : " +
                                selectedStartMinute.intValue.toString().padStart(2, '0'),
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
                            showingTimePicker.value = TimePickerType.END
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
            showingTimePicker.value?.let { pickerType ->
                Spacer(modifier = Modifier.height(16.dp))
                GrayDivider()
                Spacer(modifier = Modifier.height(16.dp))
                when (pickerType) {
                    TimePickerType.START -> CustomTimePicker(selectedHour = selectedStartHour, selectedMinute = selectedStartMinute) {
                        showingTimePicker.value = null // Picker를 닫음
                    }
                    TimePickerType.END -> CustomTimePicker(selectedHour = selectedEndHour, selectedMinute = selectedEndMinute) {
                        showingTimePicker.value = null // Picker를 닫음
                    }
                }
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
                        text = "Content",
                        color = PlaceDefault, // 텍스트의 색상을 지정
                        fontSize = 18.sp
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
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel", fontSize = 20.sp)
            }

            // 저장 버튼
            Button(
                onClick = {
                    if (scheduleName.value.isEmpty()) {
                        // alert 창으로 사용자에게 메시지를 띄우기
                        showDialogTitle = true
                        return@Button
                    }
                    val startDate = combineDateTime(selectedStartDate.value, selectedStartHour.intValue, selectedStartMinute.intValue)
                    val endDate = combineDateTime(selectedEndDate.value, selectedEndHour.intValue, selectedEndMinute.intValue)

                    if(startDate.after(endDate)) {
                        showDialogDate = true
                        return@Button
                    }

                    val schedule = Schedule(
                        scheduleSeq = null,
                        startDate = startDate,
                        endDate = endDate,
                        scheduleName = scheduleName.value,
                        wallpaperUrl = null,
                        content = content.value,
                    )


                    CoroutineScope(Dispatchers.Main).launch {
                        // 일정 추가
                        withContext(Dispatchers.IO) {
                        val scheduleSeq = scheduleViewModel.saveSchedule(schedule).await()
                            try {
                                val response = ApiObject.ImageService.createImage(CreateImageRequest(scheduleName.value, "fantasy"))
                                if (response.isSuccessful) {
                                    // 성공적으로 URL을 받아옵니다.
                                    val imageUrl = response.body().toString()
                                    // 이미지 URL이 성공적으로 받아졌다면, 업데이트 로직 수행
                                    Log.e("Seq", scheduleSeq.toString())
//                                    scheduleViewModel.updateScheduleImgUrl(scheduleSeq.toString(), imageUrl)
                                    val intent = Intent(context, UpdateImageService::class.java).apply {
                                        putExtra("scheduleSeq", scheduleSeq.toString())
                                        putExtra("newImgUrl", imageUrl)
                                    }
                                    context.startService(intent)
                                    scheduleWallpaperChange(context, startDate, imageUrl)
                                } else {
                                    Log.e("ERROR", "이미지 생성 오류: ${response.errorBody()?.string()}")
                                }
                            } catch (e: Exception) {
                                Log.e("ERROR", "이미지 생성 예외 발생", e)
                            }
                        }
                    }
                    // 사용자에게 피드백을 제공하고 화면을 닫습니다.
                    Toast.makeText(context, "Schedule has been added", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                },
//                        CoroutineScope(Dispatchers.Main).launch {
//                            // 사용자가 대기하지 않아도 되도록 withContext로 네트워크 호출을 백그라운드에서 수행합니다.
//                            withContext(Dispatchers.IO) {
//                                try {
//                                    // createImage 함수를 비동기적으로 호출하고 결과를 받아옵니다.
//                                    val response = ApiObject.ImageService.createImage(CreateImageRequest(scheduleName.value,
//                                        "ghibli"))
//                                    if (response.isSuccessful) {
//                                        // 성공적으로 URL을 받아옵니다.
//                                        response.body() ?: ""
//                                        // 일정 시작 10분 전 배경화면 바꾸기
//                                        scheduleWallpaperChange(context, startDate, response.body().toString())
//                                        scheduleViewModel.updateScheduleImgUrl(scheduleSeq.toString(), response.body()!!)
//                                    } else {
//                                        Log.e("ERROR", "이미지 생성 오류")
//                                    }
//                                } catch (e: Exception) {
//                                    Log.e("ERROR", "이미지 생성 예외 발생")
//                                }
//                            }
//                        }

                    // 일정 추가 후 뒤로가기
//                    Toast.makeText(context, "Schedule has been added", Toast.LENGTH_LONG).show()
//                    navController.popBackStack()

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Save", fontSize = 20.sp)
            }

            if (showDialogTitle) {
                CustomAlertDialog(
                    title = "Error",
                    description = "Schedule name cannot be null",
                    onConfirm = {
                        showDialogTitle = false
                    }
                )
            } else if (showDialogDate) {
                CustomAlertDialog(
                    title = "Error",
                    description = "End time cannot be earlier than start time",
                    onConfirm = {
                        showDialogDate = false
                    }
                )
            }
        }

    }
}

fun getDayOfWeek(day: DayOfWeek): String {
    return when (day) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
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

// 선택한 날짜, 시간을 Date 형식으로 바꾸기
fun combineDateTime(date: LocalDateTime, hour: Int, minute: Int): Date {
    val combinedDateTime = date.withHour(hour).withMinute(minute)
    return Date.from(combinedDateTime.atZone(ZoneId.systemDefault()).toInstant())
}
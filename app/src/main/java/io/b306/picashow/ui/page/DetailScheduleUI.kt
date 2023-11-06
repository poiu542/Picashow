package io.b306.picashow.ui.page

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.entity.Schedule
import io.b306.picashow.repository.ScheduleRepository
import io.b306.picashow.scheduleWallpaperChange
import io.b306.picashow.ui.components.CustomAlertDialog
import io.b306.picashow.ui.components.CustomTimePicker
import io.b306.picashow.ui.components.GrayDivider
import io.b306.picashow.ui.theme.PlaceDefault
import io.b306.picashow.ui.theme.TextFieldCursor
import io.b306.picashow.viewmodel.ScheduleViewModel
import io.b306.picashow.viewmodel.ScheduleViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Composable
fun DetailScheduleUI(navController : NavController, scheduleSeq: String) {

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

    // 이전에 데이터를 로드했는지 여부를 추적하는 변수
    var isDataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(scheduleSeq) {
        if (!isDataLoaded) {
            scheduleViewModel.getScheduleById(scheduleSeq) { scheduleDetails ->
                // 여기서 UI 상태를 업데이트하는 코드를 실행합니다.
                // 예를 들면:
                scheduleDetails?.let {
                    scheduleName.value = it.scheduleName ?: ""
                    content.value = it.content ?: ""

                    // 날짜 및 시간 설정
                    it.startDate?.let { startDate ->
                        // Date 객체를 LocalDateTime으로 변환
                        val startDateTime = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault())
                        selectedStartDate.value = startDateTime
                        selectedStartHour.value = startDateTime.hour
                        selectedStartMinute.value = startDateTime.minute
                    }

                    it.endDate?.let { endDate ->
                        // Date 객체를 LocalDateTime으로 변환
                        val endDateTime = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault())
                        selectedEndDate.value = endDateTime
                        selectedEndHour.value = endDateTime.hour
                        selectedEndMinute.value = endDateTime.minute
                    }
                }

                // 데이터가 로드되었음을 표시
                isDataLoaded = true
            }
        }
    }

    // AlertDialog 상태
    var showDialogTitle by remember { mutableStateOf(false) }
    var showDialogDate by remember { mutableStateOf(false) }


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
                    // 일정 Room 수정하기 - imageURL은 없음
                    scheduleViewModel.updateSchedule(scheduleSeq, schedule)
                    /* TODO
                        1. FastAPI 요청 보내서 이미지 URL 받기
                        2. 받은 URL schedule 테이블에 update로 넣기
                        3. 그 URL 기반으로 이미지 배경화면 바뀜 예약하기
                        * 주의사항
                        - 사용자가 앱을 종료하면? - background에서 돌려야 할 듯
                    */
                    val url = "https://i.pinimg.com/736x/85/d7/de/85d7de9a4a4d55a198dfcfd00a045f84.jpg"
                    val url2 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRu3WFtOVor0CH59xCanFxZ21wDCyUueV7jPg&usqp=CAU"

                    // 일정 시작 10분 전부터 배경화면 바꾸기
                    scheduleWallpaperChange(context, startDate, url)

                    Toast.makeText(context, "The schedule has been modified", Toast.LENGTH_LONG).show()

                    // 일정 수정 후 뒤로가기
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("저장", fontSize = 20.sp)
            }
            if (showDialogTitle) {
                CustomAlertDialog(
                    title = "Error",
                    description = "Schedule name cannot be null!",
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

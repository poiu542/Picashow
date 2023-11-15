package io.b101.picashow.ui.page

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import io.b101.picashow.R
import io.b101.picashow.UpdateImageService
import io.b101.picashow.api.ApiObject
import io.b101.picashow.api.image.CreateImageRequest
import io.b101.picashow.database.AppDatabase
import io.b101.picashow.entity.Schedule
import io.b101.picashow.repository.ScheduleRepository
import io.b101.picashow.repository.ThemeRepository
import io.b101.picashow.scheduleWallpaperChange
import io.b101.picashow.ui.components.CustomAlertDialog
import io.b101.picashow.ui.components.CustomTimePicker
import io.b101.picashow.ui.components.GrayDivider
import io.b101.picashow.ui.theme.PlaceDefault
import io.b101.picashow.ui.theme.TextFieldCursor
import io.b101.picashow.util.await
import io.b101.picashow.viewmodel.ScheduleViewModel
import io.b101.picashow.viewmodel.ScheduleViewModelFactory
import io.b101.picashow.viewmodel.ThemeViewModel
import io.b101.picashow.viewmodel.ThemeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    val themeDao = AppDatabase.getDatabase(context).themeDao()
    val themeRepository = ThemeRepository(themeDao)
    val themeViewModelFactory = ThemeViewModelFactory(themeRepository)
    val themeViewModel: ThemeViewModel = viewModel(factory = themeViewModelFactory)

    val selectedStartDate = remember { mutableStateOf(LocalDateTime.now()) }
    val selectedStartHour = remember { mutableIntStateOf(LocalDateTime.now().hour) }
    val selectedStartMinute = remember { mutableIntStateOf(LocalDateTime.now().minute) }
    val selectedEndHour = remember { mutableIntStateOf(LocalDateTime.now().hour) }
    val selectedEndMinute = remember { mutableIntStateOf(LocalDateTime.now().minute) }
    val selectedEndDate = remember { mutableStateOf(LocalDateTime.now()) }
    val scheduleName = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }
    val imageURL = remember { mutableStateOf("") }
    val randomKeyword = remember { mutableStateOf<String?>(null) }
    var randomKeywordState = remember { mutableStateOf(false) }

    // 시간 선택기 상태를 관리할 MutableState를 정의
    var showingTimePicker = remember { mutableStateOf<TimePickerType?>(null) }

    // 이전에 데이터를 로드했는지 여부를 추적하는 변수
    var isDataLoaded by remember { mutableStateOf(false) }

    val themeListState = themeViewModel.allKeywords.observeAsState(initial = emptyList())
    // remember를 사용하여 최초의 랜덤 키워드 선택을 기억합니다.
    LaunchedEffect(themeListState.value) {
        if (themeListState.value.isNotEmpty()) {
            // themeListState가 변경될 때만 랜덤 키워드를 갱신합니다.
            randomKeyword.value = themeListState.value.random()
        }
    }


    LaunchedEffect(scheduleSeq) {
        if (!isDataLoaded) {
            // 랜덤한 keyWord를 선택

            scheduleViewModel.getScheduleById(scheduleSeq) { scheduleDetails ->
                // 여기서 UI 상태를 업데이트하는 코드를 실행합니다.
                // 예를 들면:
                scheduleDetails?.let {
                    scheduleName.value = it.scheduleName ?: ""
                    content.value = it.content ?: ""
                    imageURL.value = it.wallpaperUrl ?: ""

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


            if(imageURL.value == "") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), // 최대 너비를 채움
                    horizontalArrangement = Arrangement.Center // 가로 방향으로 중앙 정렬
                ) {
                    Text(
                        text = "No images have been created yet.",
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.null_image),
                    contentDescription = "Displayed Image", // 접근성 설명
                    modifier = Modifier
                        .fillMaxWidth() // 이미지를 최대 너비로 채우도록 설정
                        .height(300.dp) // 원하는 높이로 설정
                )
            } else {
                Image(
                    painter = rememberImagePainter(imageURL.value), // 이미지 URL
                    contentDescription = "Displayed Image", // 접근성 설명
                    modifier = Modifier
                        .fillMaxWidth() // 이미지를 최대 너비로 채우도록 설정
                        .height(300.dp) // 원하는 높이로 설정
                )
            }

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
                    CoroutineScope(Dispatchers.Main).launch {
                        // 일정 추가
                        withContext(Dispatchers.IO) {
                            try {
                                val response = ApiObject.ImageService.createImage(CreateImageRequest(scheduleName.value, randomKeyword.value!!))
                                if (response.isSuccessful) {
                                    // 성공적으로 URL을 받아옵니다.
                                    val imageUrl = response.body().toString()
                                    // 이미지 URL이 성공적으로 받아졌다면, 업데이트 로직 수행
                                    Log.e("Seq", scheduleSeq)
                                    val intent = Intent(context, UpdateImageService::class.java).apply {
                                        putExtra("scheduleSeq", scheduleSeq)
                                        putExtra("newImgUrl", imageUrl)
                                        putExtra("kind", "schedulePage")
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
                Text("Save", fontSize = 20.sp)
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

package io.b306.picashow.ui.page

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.entity.Diary
import io.b306.picashow.repository.DiaryRepository
import io.b306.picashow.ui.theme.teal40
import io.b306.picashow.viewmodel.DiaryViewModel
import io.b306.picashow.viewmodel.DiaryViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

val inputDateTime = LocalDateTime.now()
val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val formattedDate = inputDateTime.format(formatter)
var diaryTitle = mutableStateOf(formattedDate)
var diaryDatePickerFlag = mutableStateOf(false)
var targetPage = mutableStateOf(999999)
var changeCheck= mutableStateOf(false)

@Composable
@OptIn(ExperimentalPagerApi::class)
fun DiaryPage() {
    // 의존성 주입
    val context = LocalContext.current

    val diaryDao = AppDatabase.getDatabase(context).diaryDao()
    val diaryRepository = DiaryRepository(diaryDao)
    val diaryViewModelFactory = DiaryViewModelFactory(diaryRepository)

    val diaryViewModel = viewModel<DiaryViewModel>(
        factory = diaryViewModelFactory
    )

    // 날짜 포맷터
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // 사용자가 선택한 날짜를 String으로 가져옴
    val selectedDateStr = diaryTitle.value

    // 선택한 날짜를 Date로 변환
    val selectedDate = dateFormatter.parse(selectedDateStr)
        ImageCompo(diaryViewModel = diaryViewModel) // 페이지별로 Image를 그립니다.

    val pagerState = rememberPagerState(pageCount = 2000000, initialPage = 999999)

    if(diaryDatePickerFlag.value) {ShowDatePicker(diaryTitle.value); diaryDatePickerFlag.value=false}
}




@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCompo(diaryViewModel: DiaryViewModel) {
    val diaryList = diaryViewModel.diaryList.value
    var selectedDiary: Diary? = null
    val coroutineScope = rememberCoroutineScope()
    if (!diaryList.isNullOrEmpty()) {
        selectedDiary = diaryList[0]
    }

    var userChangedTitle = remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(pageCount = 2000000, initialPage = 999999)

    HorizontalPager(state = pagerState) { page ->
        val date = LocalDate.now().plusDays(page.toLong() - 1000000L)
        diaryTitle.value = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        if(changeCheck.value) {
            coroutineScope.launch {
                pagerState.scrollToPage(targetPage.value)
                changeCheck.value = false
            }
        }
        LaunchedEffect(diaryTitle.value, pagerState.currentPage) {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDate = dateFormatter.parse(diaryTitle.value)
            diaryViewModel.getDiaryByDate(selectedDate.time)
            Log.d("무한 호출인가요? ={}", "yes")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            content = {
                item {
                    val currentDate = remember {
                        val dateFormat = SimpleDateFormat("M월 d일 EEEE", Locale.getDefault())
                        dateFormat.format(Date())
                    }

                    var imageUrl = selectedDiary?.url
                    if (imageUrl == null) {
                        imageUrl = "https://comercial-wallpaper.s3.ap-northeast-2.amazonaws.com/images/5089873592208240427.png"
                    }
                    val painter = rememberImagePainter(data = imageUrl)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    ) {
                        DateText(diaryTitle.value) { }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        Image(
                            contentScale = ContentScale.Crop,
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }

                    if (!diaryList.isNullOrEmpty()) {
                        DiaryText(selectedDiary!!, diaryViewModel, userChangedTitle)
                    } else {
                        TextPlaceHolder(diaryViewModel, userChangedTitle)
                    }
                }
            }
        )
    }
}

@Composable
fun DiaryText(diary: Diary, diaryViewModel: DiaryViewModel, userChangedTitle: MutableState<Boolean>) {
    var isEditing by remember { mutableStateOf(false) }
    var editText by remember(diary) { mutableStateOf(diary.content) }
    val coroutineScope = rememberCoroutineScope()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    LaunchedEffect(diary.content) {
        editText = diary.content
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val titleBoxModifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(1.dp))
            .padding(4.dp)

        Box(
            modifier = titleBoxModifier,
            contentAlignment = Alignment.Center
        ) {
            if (!isEditing) {
                Text(
                    text = diary.content ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),
                )
            } else {
                BasicTextField(
                    value = editText ?: "",
                    onValueChange = { newText -> editText = newText },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 90.dp)
                        .background(Color.Transparent),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(Color.White),
                    singleLine = false
                )
            }
        }

        if (!isEditing) {
            Button(
                onClick = { isEditing = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(teal40)
            ) {
                Text(text = "일기 수정")
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            diary.content = editText
                            diaryViewModel.updateDiary(diary)
                            isEditing = false
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(teal40)
                ) {
                    Text(text = "완료")
                }

                Button(
                    onClick = {
                        editText = diary.content ?: ""
                        isEditing = false
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(teal40)
                ) {
                    Text(text = "취소")
                }
            }
        }
    }
}

@Composable
fun TextPlaceHolder(viewModel: DiaryViewModel, userChangedTitle: MutableState<Boolean>) {
    var text by remember { mutableStateOf("") }

    val imageUrl = "https://comercial-wallpaper.s3.ap-northeast-2.amazonaws.com/images/5089873592208240427.png"
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val titleBoxModifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(1.dp))
            .padding(4.dp)

        Box(
            modifier = titleBoxModifier,
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = text,
                onValueChange = { newText -> text = newText },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 90.dp)
                    .background(Color.Transparent),
                visualTransformation = VisualTransformation.None,
                singleLine = false
            )
            if (text.isEmpty()) {
                Text(
                    text = "diary content",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                val diary = Diary(
                    diarySeq = null,
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(diaryTitle.value),
                    title = null,
                    content = text,
                    url = imageUrl
                )
                viewModel.saveDiary(diary)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(teal40)
    ) {
        Text(text = "일기 저장")
    }
}


@Composable
fun DateText(selectedDate: String, onDateTextClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.Transparent)
            .padding(4.dp)
            .clickable( onClick = onDateTextClicked), // Box를 클릭 가능하게 만듭니다.
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = AnnotatedString(selectedDate),
            color = Color.LightGray,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {diaryDatePickerFlag.value=true}
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowDatePicker(selectedDate: String) {
        val context = LocalContext.current

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
                        val calendar: Calendar = Calendar.getInstance()
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        val month = calendar.get(Calendar.MONTH)
                        val year = calendar.get(Calendar.YEAR)

                        DatePickerDialog(context, { _, mYear, mMonth, mDay ->
                            val newSelectedDate = LocalDate.of(mYear, mMonth + 1, mDay)
                            diaryTitle.value =
                                newSelectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                             targetPage.value = (newSelectedDate.toEpochDay() - LocalDate.now()
                                .toEpochDay()).toInt() + 999999
                            changeCheck.value = true
                        }, year, month, day).show()
                    }

            if (selectedDate.isNotEmpty()) {
                Text(
                    text = "선택된 날짜: $selectedDate",
                    modifier = Modifier
//                        .clickable { onDateClicked() }
                        .padding(start = 8.dp)
                )
            }

    }



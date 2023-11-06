package io.b306.picashow.ui.page

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.entity.Diary
import io.b306.picashow.repository.DiaryRepository
import io.b306.picashow.repository.MemberRepository
import io.b306.picashow.ui.theme.teal40
import io.b306.picashow.viewmodel.DiaryViewModel
import io.b306.picashow.viewmodel.DiaryViewModelFactory
import io.b306.picashow.viewmodel.MemberViewModel
import io.b306.picashow.viewmodel.MemberViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

val inputDateTime = LocalDateTime.now()
val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val formattedDate = inputDateTime.format(formatter)
var diaryTitle = mutableStateOf(formattedDate)

@Composable
fun DiaryPage() {
    // 의존성 주입
    val context = LocalContext.current

    val diaryDao = AppDatabase.getDatabase(context).diaryDao()
    val diaryRepository = DiaryRepository(diaryDao)
    val diaryViewModelFactory = DiaryViewModelFactory(diaryRepository)

    val diaryViewModel = viewModel<DiaryViewModel>(
        factory = diaryViewModelFactory
    )

    val memberDao = AppDatabase.getDatabase(context).memberDao()
    val memberRepository = MemberRepository(memberDao)
    val memberViewModelFactory = MemberViewModelFactory(memberRepository)

    val memberViewModel = viewModel<MemberViewModel>(
        factory = memberViewModelFactory
    )

    // 날짜 포맷터
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // 사용자가 선택한 날짜를 String으로 가져옴
    val selectedDateStr = diaryTitle.value

    // 선택한 날짜를 Date로 변환
    val selectedDate = dateFormatter.parse(selectedDateStr)

    // 선택한 날짜에 해당하는 일기 리스트를 가져옵니다.
    LaunchedEffect(selectedDateStr) {
        diaryViewModel.getDiaryByDate(selectedDate.time)
        Log.d("now time=", selectedDate.time.toString()) // Date 객체에서 time을 사용하여 Long으로 변환
    }
    val diaryList by diaryViewModel.diaryList.observeAsState()

    Log.d("selectedDate =", selectedDateStr)
    Log.d("selectedDate =", diaryList.toString())
//    Log.d("diaryList =", diaryList?.get(0)?.toString() ?: "Diary 목록이 비어있습니다.")

    Image(diaryViewModel)
}





@Composable
fun Image(diaryViewModel: DiaryViewModel) {
    val diaryList = diaryViewModel.diaryList.value
    var selectedDiary: Diary? = null

    if (!diaryList.isNullOrEmpty()) {
        // 선택한 날짜에 일기가 있을 때 표시할 내용을 작성합니다.
        selectedDiary = diaryList[0] // 여기에서 첫 번째 일기를 가져옴
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
                if(imageUrl == null) {
                    // TODO : 이거 이미지 뭐로 가져올지 정해야 됨
                    imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B"
                }
                val painter = rememberImagePainter(data = imageUrl)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    DateText(diaryTitle.value) {

                    }
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
                    // 선택한 날짜에 일기가 있을 때 표시할 내용을 작성합니다.
                    DiaryText(selectedDiary!!)
                } else {
                    // 선택한 날짜에 일기가 없을 때 새로운 일기를 작성할 수 있도록 UI를 구성합니다.
                    TextPlaceHolder(diaryViewModel)
                }
            }
        }
    )
}


@Composable
fun DiaryText(diary : Diary) {

    val content = diary.content

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 제목 출력
        val titleBoxModifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(1.dp))
            .padding(4.dp)

        Box(
            modifier = titleBoxModifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = content.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),

            )
        }
    }
}

@Composable
fun TextPlaceHolder(viewModel: DiaryViewModel) {
    var text by remember { mutableStateOf("") }

    val imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 제목 입력
        val titleBoxModifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
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
                    .background(Color.Transparent),
                visualTransformation = VisualTransformation.None
            )

            if (text.isEmpty()) {
                Text(
                    text = "text",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // 저장 버튼
    Button(
        onClick = {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            Log.e("에러에러에러", diaryTitle.value)
            Log.e("에러에러에러", currentDate)

            val diary = Diary(
                diarySeq = null, // autoGenerate로 설정되므로 null로 둡니다.
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(diaryTitle.value),
                title = text,
                content = text,
                url = imageUrl
            )
            viewModel.saveDiary(diary) // DiaryViewModel을 사용하여 일기를 저장합니다.
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            ,
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
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(1.dp))
            .padding(4.dp)
            .clickable { // DateText를 클릭할 때 onDateTextClicked() 호출
                onDateTextClicked()
            },
        contentAlignment = Alignment.Center
    ) {
        ShowDatePicker(selectedDate = diaryTitle.value)
        Text(
            text = AnnotatedString(selectedDate),
            color = Color.LightGray,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShowDatePicker(selectedDate: String) {
    val context = LocalContext.current

    Icon(
        Icons.Default.DateRange,
        contentDescription = "Open Date Picker",
        modifier = Modifier.clickable {
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            // 'selectedDate'를 'yyyy/MM/dd' 형식으로 변경
//            val selectedDateFormatted = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(
//                SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).parse(selectedDate)
//            )

            DatePickerDialog(context, { _, mYear, mMonth, mDay ->
                val newSelectedDate = "$mYear-${String.format("%02d", mMonth + 1)}-${String.format("%02d", mDay)}"
                diaryTitle.value = newSelectedDate
            }, year, month, day).show()
        }
    )

    if (selectedDate.isNotEmpty()) {
        Text("Selected Date: $selectedDate")
    }
}

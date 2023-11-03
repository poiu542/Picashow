package io.b306.picashow.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import io.b306.picashow.viewmodel.DiaryViewModel
import io.b306.picashow.viewmodel.DiaryViewModelFactory
import io.b306.picashow.viewmodel.MemberViewModel
import io.b306.picashow.viewmodel.MemberViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = {
            item {
                val currentDate = remember {
                    val dateFormat = SimpleDateFormat("M월 d일 EEEE", Locale.getDefault())
                    dateFormat.format(Date())
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    DateText()
//                    ShowDatePicker()
                }


                val imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B"

                val painter = rememberImagePainter(data = imageUrl)
                Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                    .padding(4.dp)

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
                            .clip(RoundedCornerShape(1.dp))
                            .border(1.dp, Color.Gray)
                    )
                }

                TextPlaceHolder(diaryViewModel)
            }
        }
    )
}
@Composable
fun TextPlaceHolder(viewModel: DiaryViewModel) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    val imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B"
    val painter = rememberImagePainter(data = imageUrl)

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
                value = title,
                onValueChange = { newText -> title = newText },
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

            if (title.isEmpty()) {
                Text(
                    text = "Title",
                    color = Color.Gray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 내용 입력 박스
        val textBoxModifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(1.dp))
            .padding(4.dp)

        Box(
            modifier = textBoxModifier,
            contentAlignment = Alignment.BottomCenter
        ) {
            // 밑줄
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )

            // 텍스트 입력 필드
            BasicTextField(
                value = text,
                onValueChange = { newText -> text = newText },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Left // 왼쪽 정렬
                ),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(top = 4.dp) // 밑줄과 간격 띄우기
                ,
                visualTransformation = VisualTransformation.None
            )
        }
    }

    // 저장 버튼
    Button(
        onClick = {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val diary = Diary(
                null,
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentDate),
                title = title,
                content = text,
                url = imageUrl
            )
            viewModel.saveDiary(diary)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "일기 저장")
    }
}


@Composable
fun DateText() {
    val currentDate = remember {
        val dateFormat = SimpleDateFormat("M월 d일 EEEE", Locale.getDefault())
        dateFormat.format(Date())
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.Transparent)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(1.dp))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$currentDate Diary",
            color = Color.LightGray,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}
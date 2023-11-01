package io.b306.picashow.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import io.b306.picashow.R
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



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(), // 전체 화면 크기로 설정
            content = {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    val imageUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B"

                    val painter = rememberImagePainter(data = imageUrl)
                    Image(
                        contentScale = ContentScale.Crop,
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth() // 가로 너비를 부모 컨테이너에 맞게 설정
                            .height(350.dp) // 이미지의 높이를 화면 높이의 절반으로 설정
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TextPlaceHolder(diaryViewModel)
                }
            }
        )
    }
}

@Composable
fun TextPlaceHolder(viewModel: DiaryViewModel) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 제목 입력
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
            visualTransformation = VisualTransformation.None,
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Title",
                            color = Color.Gray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                innerTextField()
            }
        )

        // 날짜 표시 (yyyy-MM-dd 형식)
        val currentDate = remember {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        }
        Text(
            text = "$currentDate Diary",
            color = Color.LightGray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 내용 입력
        BasicTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            decorationBox = { innerTextField ->
                if (text.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Type your text here",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                innerTextField()
            })
    }

    // 저장 버튼
    Button(
        onClick = {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val diary = Diary(null,
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentDate),
                title = title,
                content = text,
                url = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B"
            )
            viewModel.saveDiary(diary)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Save Diary")
    }
}


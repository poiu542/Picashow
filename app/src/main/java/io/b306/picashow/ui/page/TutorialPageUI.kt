package io.b306.picashow.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import io.b306.picashow.R
import io.b306.picashow.ui.theme.Purple40
import io.b306.picashow.ui.theme.teal40

var tutorialImageUrls = mutableListOf(
    arrayOf(
    "https://mblogthumb-phinf.pstatic.net/MjAxOTAzMTlfMjYy/MDAxNTUyOTI4OTcyMDgw.USYJbzqsCIe02lwsQ1qNSodtNFKKeaIumsOMNio-ffcg.g27f78YWMeUuZCjOh8PeZrMODh6o1S8AtOEXDENwb9og.JPEG.hongseull/%EB%A7%88%EB%85%80%EB%B0%B0%EB%8B%AC%EB%B6%80_%ED%82%A4%ED%82%A4.jpg?type=w800",
    "https://i.pinimg.com/564x/5e/7c/e1/5e7ce110221f9dbc96579c978da62aa8.jpg",
    "https://i.pinimg.com/originals/22/de/92/22de92092890e1540ca5015818f5c3cd.jpg",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRu3WFtOVor0CH59xCanFxZ21wDCyUueV7jPg&usqp=CAU",
    "https://images.unsplash.com/photo-1659951345629-091600ae202c?auto=format&fit=crop&q=80&w=435&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    "https://mblogthumb-phinf.pstatic.net/MjAxODAxMjFfMTMy/MDAxNTE2NTQyOTA0Mzk3.sodrPAj7QOabX0S6tKObbbAGo9xXkX3QiauDEU0ShTgg.olNXD3GdYDF3JH9C36dnI1NUuuJdcfv61uBTCVR2c1Eg.JPEG.knicjin/20180121-010.jpg?type=w800",
    ),
    arrayOf(
    "zebri",
    "europe",
    "future",
    "science",
    "greece",
    "sky"
    )
        )

@Composable
fun tutorialPage() {
    LaunchedEffect(Unit) {  // 이 키워드는 Composable 내부에서 새로운 코루틴을 시작합니다.
    }
    // TODO 튜토리얼 페이지
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Choose the photos you like", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        Spacer(modifier = Modifier.height(10.dp))
        TutorialImageListFromUrls()
    }
}

@Composable
fun TutorialImageListFromUrls() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val selectedImageIndices = remember { mutableStateListOf<Int>() }

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {
        for (i in tutorialImageUrls[0].indices step 3) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (j in i until minOf(i + 3, tutorialImageUrls[0].size)) {
                    val imageUrl = tutorialImageUrls[0][j]
                    val isSelected = j in selectedImageIndices
                    Image(
                        contentScale = ContentScale.Crop,
                        painter = rememberImagePainter(
                            data = imageUrl,
                            builder = {
                                crossfade(true)
//                              placeholder(R.drawable.waiting_image)
                            }
                        ),
                        contentDescription = "인공지능이 생성한 바탕화면",
                        modifier = Modifier
                            .size((screenWidth / 2) - 30.dp, ((screenWidth / 2) + 30.dp))
                            .fillMaxHeight()
                            .padding(start= 15.dp,end = 15.dp, bottom = 25.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {
                                if (isSelected) {
                                    selectedImageIndices.remove(j) // 이미 선택된 이미지라면 제거
                                } else {
                                    selectedImageIndices.add(j) // 선택되지 않은 이미지라면 추가
                                }
                            }
                            .border(
                                width = (4).dp,
                                color = if (isSelected) teal40 else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            }
        }
    }
}

package io.b306.picashow.ui.page

import android.annotation.SuppressLint
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import io.b306.picashow.database.AppDatabase
import io.b306.picashow.entity.Member
import io.b306.picashow.entity.Theme
import io.b306.picashow.flag
import io.b306.picashow.repository.MemberRepository
import io.b306.picashow.repository.ThemeRepository
import io.b306.picashow.ui.theme.teal40
import io.b306.picashow.viewmodel.MemberViewModel
import io.b306.picashow.viewmodel.MemberViewModelFactory
import io.b306.picashow.viewmodel.ThemeViewModel
import io.b306.picashow.viewmodel.ThemeViewModelFactory
import io.b306.picashow.viewmodel._myInfo
import kotlinx.coroutines.delay
import androidx.compose.ui.window.Dialog as Dialog

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

var selectedImageIndices = mutableStateListOf<Int>()
var textTutorialDone = mutableStateOf(false)
@SuppressLint("SuspiciousIndentation")
@Composable
fun tutorialPage() {
    LaunchedEffect(Unit) {  // 이 키워드는 Composable 내부에서 새로운 코루틴을 시작합니다.
    }
    if(textTutorialDone.value) mainTutorial()
    else textTutorial()

}

@Composable
fun mainTutorial() {
    val context = LocalContext.current

    val themeDao = AppDatabase.getDatabase(context).themeDao()
    val themeRepository = ThemeRepository(themeDao)
    val themeViewModelFactory = ThemeViewModelFactory(themeRepository)

    val themeViewModel = viewModel<ThemeViewModel>(
        factory = themeViewModelFactory
    )

    val memberDao = AppDatabase.getDatabase(context).memberDao()
    val memberRepository = MemberRepository(memberDao)
    val memberViewModelFactory = MemberViewModelFactory(memberRepository)

    val memberViewModel = viewModel<MemberViewModel>(
        factory = memberViewModelFactory
    )
    // TODO 튜토리얼 페이지
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            Text(
                text = "Please choose the images you like.",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(10.dp))
            // AnimatedDialog()
            TutorialImageListFromUrls()
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(60.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(color = Color.Black)
                .clickable {
                    if(selectedImageIndices.isEmpty())  Toast.makeText(context, "Please select one or more preferred images.", Toast.LENGTH_SHORT).show()
                    else {
                        flag.value = true

                        var themeList = mutableStateListOf<Theme>()
                        for (i in selectedImageIndices) {
                            val selectTheme = Theme(null, tutorialImageUrls[1][i])
                            themeList.add(selectTheme)
                        }
                        themeViewModel.insertAllThemes(themeList)
                        val deviceUniqueId = Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )

                        var member = Member(1, true, deviceUniqueId)
                        memberViewModel.saveMember(member)
                        Log.d("member = {}", _myInfo.value?.isTutorial.toString())
                    }
                }
        ) {
            var backgroundColor = if (selectedImageIndices.isEmpty()) Color.Gray else teal40
            Box(modifier = Modifier
                .clip(shape = RoundedCornerShape(10.dp))
                .height(50.dp)
                .align(Alignment.BottomCenter)
                .background(backgroundColor)
                .fillMaxSize()

            ) {
                Text(
                    text = "start",
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = Color.White,
                    fontSize = 22.sp
                )
            }
        }
    }
}

@Composable
fun textTutorial() {
    var isVisible1 by remember { mutableStateOf(false) }
    var isVisible2 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100L)
        isVisible1 = true
        delay(2000L)

        isVisible1 = false
        delay(1500L)

        isVisible2 = true
        delay(3000L)

        isVisible2 = false
        delay(1500L)
        textTutorialDone.value =true
    }

    Dialog(onDismissRequest = {}) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(500.dp)
        ) {
            AnimatedVisibility(
                visible = isVisible1,
                enter = fadeIn(animationSpec = tween(1500)),
                exit = fadeOut(animationSpec = tween(1500))
            ) {
                Text(
                    text = "welcome to free AI background!",
                    color = Color.White,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center
                )
            }

            AnimatedVisibility(
                visible = isVisible2,
                enter = fadeIn(animationSpec = tween(1500)),
                exit = fadeOut(animationSpec = tween(1500))
            ) {
                Text(
                    text = "For optimal AI background creation, \n \n show your preferences!",
                    color = Color.White,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun TutorialImageListFromUrls() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {
        for (i in tutorialImageUrls[0].indices step 2) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (j in i until minOf(i + 2, tutorialImageUrls[0].size)) {
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
                            .size((screenWidth / 2) - 30.dp, ((screenWidth / 2) + 60.dp))
                            .fillMaxHeight()
                            .padding(5.dp)
                            .clip(shape = RoundedCornerShape(5.dp))
                            .clickable {
                                if (isSelected) {
                                    selectedImageIndices.remove(j)
                                    // 이미 선택된 이미지라면 제거
                                } else {
                                    selectedImageIndices.add(j)
                                    // 선택되지 않은 이미지라면 추가
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
        Spacer(modifier = Modifier.height(50.dp))
    }
}

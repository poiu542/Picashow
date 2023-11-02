package io.b306.picashow.ui.page

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.b306.picashow.R
import io.b306.picashow.WallpaperChangeWorker
import io.b306.picashow.api.ApiObject
import java.util.Calendar

// 모든 배경 화면 URL 배열
var imageUrls = mutableListOf(
    "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B",
    "https://images.unsplash.com/photo-1659951345629-091600ae202c?auto=format&fit=crop&q=80&w=435&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    "https://mblogthumb-phinf.pstatic.net/MjAxODAxMjFfMTMy/MDAxNTE2NTQyOTA0Mzk3.sodrPAj7QOabX0S6tKObbbAGo9xXkX3QiauDEU0ShTgg.olNXD3GdYDF3JH9C36dnI1NUuuJdcfv61uBTCVR2c1Eg.JPEG.knicjin/20180121-010.jpg?type=w800",
    "https://i.pinimg.com/736x/85/d7/de/85d7de9a4a4d55a198dfcfd00a045f84.jpg",
    "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99DBC5375AC8CA3328",
    "https://i.pinimg.com/originals/2e/ba/8c/2eba8c6bc08626a0929b83347eff3b05.jpg",
  )

// 랜덤 이미지를 추가로 가져오기 위한 변수
var randomImageLoading = mutableStateOf(true) // 랜덤 이미지 상태 관리
var showBigImage =  mutableStateOf(false) // 이미지 크게 보기 상태 관리
var showDownloadDialog =  mutableStateOf(false) // 다운로드 다이얼로그 상태 관리
var selectedImageUrl=  mutableStateOf("") // 선택된 이미지의 URL
var selectedImageIndex =  mutableIntStateOf(0) // 선택된 이미지의 인덱스

@Composable
fun firstPage() {
    // 랜더링 이전에 랜덤 사진 요청
    LaunchedEffect(Unit) {
        randomImage()
        randomImageLoading.value = false
    }
    // Loading 상태 초기화
    DisposableEffect(Unit) {
        onDispose {
            randomImageLoading.value = true
            // 여기에 실행해야 할 코드를 작성하세요.
        }
    }
    // TODO 첫번째 캘린더 - 두현이 페이지
//    scheduleWallpaperChange(LocalContext.current);
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "All BACKGROUND", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            ShowDatePicker()
//            ShowTimePicker()
        }
        if (!randomImageLoading.value) {ImageListFromUrls(imageUrls)}
        Dialog();
        DownLoadDialog();
    }

}

suspend fun randomImage() {
    try {
        val response = ApiObject.ImageService.getRandomImages("93YL0o_9XZVVDjcByIys84JINkWnLKTGXML9PNMXES4")
        val urlList = response.body()?.urls
        urlList?.let { imageUrls.add(it.raw)}
    }catch (e: Exception) {
        Log.d("randomImage 오류 발생",e.printStackTrace().toString())
    }

}

@Composable
fun ShowDatePicker() {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }

    Icon(
        Icons.Default.DateRange,
        contentDescription = "Open Date Picker",
        tint = Color.White,
        modifier = Modifier.clickable {
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            DatePickerDialog(context, { _, mYear, mMonth, mDay ->
                selectedDate = "${mDay}/${mMonth + 1}/$mYear"
                // selectedDate 변수에 선택된 날짜가 저장됩니다.
                // 이 변수를 원하는 곳에서 사용하세요.

            }, year, month, day).show()
        }
    )
    if (selectedDate.isNotEmpty()) {
        Text("Selected Date: $selectedDate")
    }
}

@Composable
fun ShowTimePicker() {
    val context = LocalContext.current
    var selectedTime by remember { mutableStateOf("") }
    Text(text = "jjex",
         color = Color.White
    )

    Icon(
        Icons.Rounded.Build,
        contentDescription = "Open Time Picker",
        tint = Color.White,
        modifier = Modifier.clickable {
            val calendar: Calendar = Calendar.getInstance()
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(context, { _, mHour, mMinute ->
                selectedTime= "${mHour}:${mMinute}"
                // selectedTime 변수에 선택된 시간이 저장됩니다.
                // 이 변수를 원하는 곳에서 사용하세요.

            }, hourOfDay, minute, true).show()
        }
    )
    if (selectedTime.isNotEmpty()) {
        Text("Selected Date: $selectedTime")
    }
}

@Composable
fun DownLoadDialog() {
    val context = LocalContext.current
    if (showDownloadDialog.value) {
        Dialog(
            onDismissRequest = { showDownloadDialog.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // experimental
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Transparent)
                    .clickable { showDownloadDialog.value = false }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(230.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black)
                        .clickable {}
                ) {
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .width(150.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, start = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        Text(
                            fontSize = 20.sp,
                            text = "\uD83D\uDCF1  배경화면으로 설정",
                            color = Color.White,
                            modifier = Modifier.clickable {
                                val imageUrl = selectedImageUrl.value // 변경하려는 이미지의 URL
                                val inputData = workDataOf("imageUrl" to imageUrl)
                                val changeWallpaperRequest = OneTimeWorkRequestBuilder<WallpaperChangeWorker>()
                                    .setInputData(inputData)
                                    .build()
                                WorkManager.getInstance(context).enqueue(changeWallpaperRequest)
                                Toast.makeText(context, "해당 이미지가 배경화면으로 설정되었습니다.", Toast.LENGTH_SHORT).show()
                            },
                        )

                        Text(
                            fontSize = 20.sp,
                            text = "\uD83D\uDD12  잠금화면으로 설정",
                            modifier = Modifier.clickable {
                                // 잠금화면으로 설정하는 코드를 여기에 작성하세요
                            },
                            color = Color.White
                        )

                        Text(
                            fontSize = 20.sp,
                            text = "\uD83D\uDDBC️  내 갤러리에 저장",
                            modifier = Modifier.clickable {
                                downloadImage(context, selectedImageUrl.value, "배경 화면 다운로드 중", "Downloading images..")
                                Toast.makeText(context, "다운로드가 진행중입니다.", Toast.LENGTH_SHORT).show()
                            },
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Dialog() {
    val pagerState = rememberPagerState(pageCount = imageUrls.size)
    if (showBigImage.value) {
        Dialog(
            onDismissRequest = {
                showBigImage.value = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // experimental
            )
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    HorizontalPager(state = pagerState) { page ->
                        Image(
                            painter = rememberImagePainter(
                                data = imageUrls[page],
                                builder = { crossfade(true) }
                            ),
                            contentDescription = "인공지능이 생성한 바탕화면",
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(modifier=Modifier.align(Alignment.BottomCenter)) {
                            Image(
                                painter=painterResource(id=R.drawable.download),
                                contentDescription=null,
                                modifier= Modifier
                                    .size(100.dp)
                                    .padding(bottom = 50.dp)
                                    .clickable {
                                        showDownloadDialog.value = true;
                                        selectedImageUrl.value = imageUrls[page];
                                    }

                            )
                        }
                    }
                    LaunchedEffect(Unit) {
                        pagerState.scrollToPage(selectedImageIndex.value)
                    }
                }
            }
        }
    }
}

@Composable
fun ImageListFromUrls(imageList: List<String>) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {
        for (i in imageList.indices step 3) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                for (j in i until minOf(i + 3, imageList.size)) {
                    val imageUrl = imageList[j]

                    Image(
                        painter = rememberImagePainter(
                            data = imageUrl,
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = "인공지능이 생성한 바탕화면",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size((screenWidth / 3) - 17.dp, ((screenWidth / 2) - 5.dp))
                            .fillMaxHeight()
                            .padding(2.5.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {
                                selectedImageUrl.value = imageUrl
                                selectedImageIndex.value = j
                                showBigImage.value = true
                            }
                    )
                }
            }
        }
    }
}


fun downloadImage(context: Context, url: String, title: String, description: String) {
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle(title)
        .setDescription(description)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title)
        .setAllowedOverMetered(true)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}



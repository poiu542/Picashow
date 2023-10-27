package io.b306.picashow.ui.page

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import io.b306.picashow.R
import io.b306.picashow.WallpaperChangeWorker
import io.b306.picashow.scheduleWallpaperChange
import java.util.Calendar

@Composable
fun firstPage() {
    // TODO 첫번째 캘린더 - 두현이 페이지
//    scheduleWallpaperChange(LocalContext.current);
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
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
        val imageUrls = listOf(
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B",
            "https://i.pinimg.com/originals/2e/ba/8c/2eba8c6bc08626a0929b83347eff3b05.jpg",
            "https://mblogthumb-phinf.pstatic.net/MjAxODAxMjFfMTMy/MDAxNTE2NTQyOTA0Mzk3.sodrPAj7QOabX0S6tKObbbAGo9xXkX3QiauDEU0ShTgg.olNXD3GdYDF3JH9C36dnI1NUuuJdcfv61uBTCVR2c1Eg.JPEG.knicjin/20180121-010.jpg?type=w800",
            "https://i.pinimg.com/736x/85/d7/de/85d7de9a4a4d55a198dfcfd00a045f84.jpg",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99DBC5375AC8CA3328",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B",
            "https://i.pinimg.com/originals/2e/ba/8c/2eba8c6bc08626a0929b83347eff3b05.jpg",
            "https://mblogthumb-phinf.pstatic.net/MjAxODAxMjFfMTMy/MDAxNTE2NTQyOTA0Mzk3.sodrPAj7QOabX0S6tKObbbAGo9xXkX3QiauDEU0ShTgg.olNXD3GdYDF3JH9C36dnI1NUuuJdcfv61uBTCVR2c1Eg.JPEG.knicjin/20180121-010.jpg?type=w800",
            "https://i.pinimg.com/736x/85/d7/de/85d7de9a4a4d55a198dfcfd00a045f84.jpg",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99DBC5375AC8CA3328",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B",
            "https://i.pinimg.com/originals/2e/ba/8c/2eba8c6bc08626a0929b83347eff3b05.jpg",
            "https://mblogthumb-phinf.pstatic.net/MjAxODAxMjFfMTMy/MDAxNTE2NTQyOTA0Mzk3.sodrPAj7QOabX0S6tKObbbAGo9xXkX3QiauDEU0ShTgg.olNXD3GdYDF3JH9C36dnI1NUuuJdcfv61uBTCVR2c1Eg.JPEG.knicjin/20180121-010.jpg?type=w800",
            "https://i.pinimg.com/736x/85/d7/de/85d7de9a4a4d55a198dfcfd00a045f84.jpg",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99DBC5375AC8CA3328",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B",
            "https://i.pinimg.com/originals/2e/ba/8c/2eba8c6bc08626a0929b83347eff3b05.jpg",
            "https://mblogthumb-phinf.pstatic.net/MjAxODAxMjFfMTMy/MDAxNTE2NTQyOTA0Mzk3.sodrPAj7QOabX0S6tKObbbAGo9xXkX3QiauDEU0ShTgg.olNXD3GdYDF3JH9C36dnI1NUuuJdcfv61uBTCVR2c1Eg.JPEG.knicjin/20180121-010.jpg?type=w800",
            "https://i.pinimg.com/736x/85/d7/de/85d7de9a4a4d55a198dfcfd00a045f84.jpg",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99DBC5375AC8CA3328",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99CD22415AC8CA2E2B",
            "https://i.pinimg.com/originals/2e/ba/8c/2eba8c6bc08626a0929b83347eff3b05.jpg",
            "https://mblogthumb-phinf.pstatic.net/MjAxODAxMjFfMTMy/MDAxNTE2NTQyOTA0Mzk3.sodrPAj7QOabX0S6tKObbbAGo9xXkX3QiauDEU0ShTgg.olNXD3GdYDF3JH9C36dnI1NUuuJdcfv61uBTCVR2c1Eg.JPEG.knicjin/20180121-010.jpg?type=w800",
            "https://i.pinimg.com/736x/85/d7/de/85d7de9a4a4d55a198dfcfd00a045f84.jpg",
            "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Ft1.daumcdn.net%2Fcfile%2Ftistory%2F99DBC5375AC8CA3328",
        )
        ImageListFromUrls(imageUrls)
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
fun ImageFromUrl() {
    val context = LocalContext.current
    val imageUrl = "https://i.namu.wiki/i/w11dbZZeomJI4bD3_KItw3vq7tgglcM1YQA_xHULxMsixPpY1S7KcB8WrEFhJNuSuejiiQkicGKMH12JvpUqBQ.webp"

    Image(
        painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(true)
                placeholder(R.drawable.waiting_image)
            }
        ),
        contentDescription = "인공지능이 생성한 바탕화면",
        modifier = Modifier
            .size(300.dp)
            .clickable {


                downloadImage(context, imageUrl, "Image Title", "Downloading...")
            }
    )
}


@Composable
fun dialog() {}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageListFromUrls(imageUrls: List<String>) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf("") }
    var selectedImageIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = imageUrls.size)

    if (showDownloadDialog) {
        Dialog(
            onDismissRequest = { showDownloadDialog = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // experimental
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Transparent)
                    .clickable { showDownloadDialog = false }
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
                            .padding(top=40.dp, start = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
                        Text(
                            fontSize = 20.sp,
                            text = "\uD83D\uDCF1  배경화면으로 설정",
                            color = Color.White,
                            modifier = Modifier.clickable {
                                val imageUrl = selectedImageUrl // 변경하려는 이미지의 URL

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
                                // 저장하는 코드를 여기에 작성하세요
                                downloadImage(context, selectedImageUrl, "배경 화면 다운로드 중", "Downloading images..")
                                Toast.makeText(context, "다운로드가 진행중입니다.", Toast.LENGTH_SHORT).show()
                            },
                            color = Color.White
                        )
                    }


                }
            }
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
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
                        Image(
                            painter = painterResource(id = R.drawable.download),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clickable { showDownloadDialog = true; selectedImageUrl = imageUrls[page];}
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 50.dp)
                        )
                    }
                    LaunchedEffect(Unit) {
                        pagerState.scrollToPage(selectedImageIndex)
                    }
                }
            }
        }
    }

    Column(
        Modifier
            .padding(2.dp)
            .verticalScroll(rememberScrollState())
    ) {
        for (i in imageUrls.indices step 3) {
            Row(Modifier.fillMaxWidth()) {
                for (j in i until minOf(i + 3, imageUrls.size)) {
                    val imageUrl = imageUrls[j]

                    Image(
                        painter = rememberImagePainter(
                            data = imageUrl,
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = "인공지능이 생성한 바탕화면",
                        modifier = Modifier
                            .size(120.dp, 200.dp)
                            .fillMaxHeight()
                            .padding(5.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable {
                                selectedImageUrl = imageUrl
                                selectedImageIndex = j
                                showDialog = true
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



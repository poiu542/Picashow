package io.b306.picashow.ui.page

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import io.b306.picashow.R
import scheduleWallpaperChange
import java.util.Calendar

@Composable
fun firstPage() {
    // TODO 첫번째 캘린더 - 두현이 페이지
    scheduleWallpaperChange(LocalContext.current);
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ShowDatePicker()
            ShowTimePicker()
        }
        ImageFromUrl()
    }
}

@Composable
fun ShowDatePicker() {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }

    Icon(
        Icons.Default.DateRange,
        contentDescription = "Open Date Picker",
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

    Icon(
        Icons.Rounded.Build,
        contentDescription = "Open Time Picker",
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



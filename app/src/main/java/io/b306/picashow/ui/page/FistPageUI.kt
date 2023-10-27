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
import io.b306.picashow.scheduleWallpaperChange
import io.b306.picashow.ui.components.ShowDatePicker
import io.b306.picashow.ui.components.ShowTimePicker
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



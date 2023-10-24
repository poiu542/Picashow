import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.b306.picashow.WallpaperChangeWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleWallpaperChange(context: Context) {
    val times = listOf(1L, 2L)

    for (time in times) {
        val delay = if (time > getCurrentHour()) {
            time - getCurrentHour()
        } else {
            time + (24 - getCurrentHour())
        }

        Log.d("delay",getCurrentHour().toString());
        Log.d("delay",delay.toString());
        val workRequest = OneTimeWorkRequestBuilder<WallpaperChangeWorker>()
            .setInitialDelay(20L*time, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "wallpaperChange$time",
            ExistingWorkPolicy.REPLACE,
            workRequest)
    }
}

fun getCurrentHour(): Long {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong()
}



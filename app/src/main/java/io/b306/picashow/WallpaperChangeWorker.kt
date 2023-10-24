package io.b306.picashow

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class WallpaperChangeWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        // 배경화면 변경 로직을 여기에 작성하세요.
        // 예시:
        changeWallpaper()
        Log.d("바뀌어요","jex")
        return Result.success()
    }

    private fun changeWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.today)
        wallpaperManager.setBitmap(bitmap)
    }
}
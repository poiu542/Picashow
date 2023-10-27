package io.b306.picashow

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class WallpaperChangeWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        changeWallpaper()
        Log.d("바뀌어요","바탕화면 변경")
        return Result.success()
    }

    private fun changeWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.today)
        wallpaperManager.setBitmap(bitmap)
    }
}
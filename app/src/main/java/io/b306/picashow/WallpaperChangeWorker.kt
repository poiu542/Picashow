package io.b306.picashow

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.IOException

class WallpaperChangeWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val imageUrl = inputData.getString("imageUrl") // WorkRequest에서 전달받은 이미지 URL
        if (imageUrl != null) {
            changeWallpaper(imageUrl)
        }
        return Result.success()
    }

    private fun changeWallpaper(imageUrl: String) {
        Glide.with(applicationContext)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                    try {
                        wallpaperManager.setBitmap(resource)
                        Log.d("바뀌어요","바탕화면 변경")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // Implement if needed
                }
            })
    }
}
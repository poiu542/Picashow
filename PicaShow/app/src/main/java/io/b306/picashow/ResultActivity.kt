package io.b306.picashow

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        var imageVal : Int = 0
        val wallpaperManager = WallpaperManager.getInstance(this)

        val oneIv : ImageView = findViewById(R.id.iv_one)
        oneIv.setOnClickListener {

            when (imageVal) {
                0 -> oneIv.setImageResource(R.drawable.hiking)
                1 -> oneIv.setImageResource(R.drawable.business)
                2 -> oneIv.setImageResource(R.drawable.study)
                3 -> oneIv.setImageResource(R.drawable.health)
            }

            val currentDrawable = oneIv.drawable
            if (currentDrawable is BitmapDrawable) {
                val originalBitmap = currentDrawable.bitmap

                // 디바이스의 화면 해상도를 구합니다.
                val displayMetrics = resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels

                // 새로운 Bitmap을 생성하고 중앙에 원본 이미지를 배치합니다.
                val centeredBitmap = Bitmap.createBitmap(screenWidth, screenHeight, originalBitmap.config)
                val canvas = Canvas(centeredBitmap)

                val imageWidth = originalBitmap.width
                val imageHeight = originalBitmap.height

                val screenAspectRatio = screenWidth.toFloat() / screenHeight
                val imageAspectRatio = imageWidth.toFloat() / imageHeight

                val scaledBitmap: Bitmap = if (imageAspectRatio > screenAspectRatio) {
                    // 이미지의 가로가 화면에 맞게 확대/축소됩니다.
                    val newHeight = (screenWidth / imageAspectRatio).toInt()
                    Bitmap.createScaledBitmap(originalBitmap, screenWidth, newHeight, true)
                } else {
                    // 이미지의 세로가 화면에 맞게 확대/축소됩니다.
                    val newWidth = (screenHeight * imageAspectRatio).toInt()
                    Bitmap.createScaledBitmap(originalBitmap, newWidth, screenHeight, true)
                }

                val x = (screenWidth - scaledBitmap.width) / 2f
                val y = (screenHeight - scaledBitmap.height) / 2f
                canvas.drawBitmap(scaledBitmap, x, y, null)

                wallpaperManager.setBitmap(scaledBitmap)
            }

            imageVal++
            if (imageVal > 3) {
                imageVal = 0
            }
        }
    }
}
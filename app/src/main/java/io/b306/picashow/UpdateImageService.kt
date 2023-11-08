package io.b306.picashow

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class UpdateImageService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val scheduleSeq = intent?.getStringExtra("scheduleSeq")
        val newImgUrl = intent?.getStringExtra("newImgUrl")
        Log.d("UpdateImageService", "onStartCommand: scheduleSeq=$scheduleSeq, newImgUrl=$newImgUrl")

        if (scheduleSeq != null && newImgUrl != null) {
            serviceScope.launch {
                try {
                    updateScheduleImgUrl(scheduleSeq, newImgUrl)
                } catch (e: Exception) {
                    Log.e("UpdateImageService", "Exception in updateScheduleImgUrl", e)
                }
                stopSelf(startId)
            }
        }

        return START_NOT_STICKY
    }

    private suspend fun updateScheduleImgUrl(scheduleSeq: String, newImgUrl: String) {
        Log.d("UpdateImageService", "Updating image URL...")
        // Application 인스턴스를 가져옵니다.
        val app = applicationContext as PicaShowApp
        // Repository에 접근합니다.
        val scheduleRepository = app.scheduleRepository
        // Repository의 함수를 호출하여 이미지 URL을 업데이트합니다.
        scheduleRepository.updateScheduleImgUrl(scheduleSeq, newImgUrl)
        Log.d("UpdateImageService", "Image URL updated")
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel() // 서비스가 종료될 때 코루틴을 취소합니다.
    }
}
package io.b306.picashow

import android.app.Application
import io.b306.picashow.database.AppDatabase

class PicaShowApp:Application() {
    val db by lazy {
        AppDatabase.getDatabase(this)
    }
}
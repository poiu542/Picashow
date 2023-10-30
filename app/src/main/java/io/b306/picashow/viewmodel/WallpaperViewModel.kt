package io.b306.picashow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.b306.picashow.entity.Schedule
import io.b306.picashow.entity.Wallpaper
import io.b306.picashow.repository.DiaryRepository
import io.b306.picashow.repository.WallpaperRepository

class WallpaperViewModel(private val repository: WallpaperRepository) : ViewModel() {
    private val _myInfo = MutableLiveData<Wallpaper?>()
    val myInfo: LiveData<Wallpaper?> get() = _myInfo
}
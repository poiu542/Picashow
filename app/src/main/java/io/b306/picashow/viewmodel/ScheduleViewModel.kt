package io.b306.picashow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.b306.picashow.entity.Member
import io.b306.picashow.entity.Schedule
import io.b306.picashow.repository.DiaryRepository
import io.b306.picashow.repository.ScheduleRepository

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
    private val _myInfo = MutableLiveData<Schedule?>()
    val myInfo: LiveData<Schedule?> get() = _myInfo
}
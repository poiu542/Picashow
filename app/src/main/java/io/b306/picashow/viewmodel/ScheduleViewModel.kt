package io.b306.picashow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.b306.picashow.entity.Member
import io.b306.picashow.entity.Schedule
import io.b306.picashow.repository.DiaryRepository
import io.b306.picashow.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {
    private val _myInfo = MutableLiveData<Schedule?>()
    val myInfo: LiveData<Schedule?> get() = _myInfo

    fun saveSchedule(schedule: Schedule) {
        viewModelScope.launch {
            // Diary를 저장하고 저장된 Diary 객체를 _myInfo LiveData에 할당
            val saveSchedule = repository.insert(schedule)
            _myInfo.value = schedule
        }
    }
}
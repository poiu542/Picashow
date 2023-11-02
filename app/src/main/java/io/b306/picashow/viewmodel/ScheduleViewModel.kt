package io.b306.picashow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import io.b306.picashow.entity.Member
import io.b306.picashow.entity.Schedule
import io.b306.picashow.repository.DiaryRepository
import io.b306.picashow.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {

    private val _schedules = MutableLiveData<List<Schedule>>()

    val schedules: LiveData<List<Schedule>> = _schedules

    fun saveSchedule(schedule: Schedule) {
        viewModelScope.launch {
            // Diary를 저장하고 저장된 Diary 객체를 _myInfo LiveData에 할당
            repository.insert(schedule)

            // 현재 LiveData를 방금 저장한 일정으로 갱신
            _schedules.value = listOf(schedule)
        }
    }

    fun fetchSchedulesForDate(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            val date = LocalDate.of(year, month, day)
            repository.getSchedulesByDate(date).collect { schedulesForDate ->
                _schedules.postValue(schedulesForDate)
            }
        }
    }

}
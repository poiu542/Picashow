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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

//    fun getScheduleById(id: String): LiveData<Schedule?> {
//        val result = MutableLiveData<Schedule?>()
//        viewModelScope.launch(Dispatchers.IO) {
//            // 백그라운드 스레드에서 데이터베이스 작업을 실행
//            result.postValue(repository.getScheduleById(id))
//        }
//        return result
//    }
    fun getScheduleById(id: String, onScheduleLoaded: (Schedule?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            // 백그라운드 스레드에서 데이터베이스 작업을 실행
            val schedule = repository.getScheduleById(id)
            withContext(Dispatchers.Main) {
                // UI 스레드에서 UI 상태를 업데이트
                onScheduleLoaded(schedule)
            }
        }
    }


//    fun updateSchedule(scheduleSeq: String, updatedScheduleData: Schedule) {
//        viewModelScope.launch {
//            val existingSchedule = repository.getScheduleById(scheduleSeq)
//            existingSchedule?.let { schedule ->
//                // 업데이트할 속성 설정
//                schedule.scheduleName = updatedScheduleData.scheduleName
//                schedule.content = updatedScheduleData.content
//                schedule.endDate = updatedScheduleData.endDate
//                schedule.startDate = updatedScheduleData.startDate
//                // 이거 나중에 이미지 다시 뽑기 할 거면 아래 주석 풀면 됨
//                // schedule.wallpaperUrl = updatedScheduleData.wallpaperUrl
//
//                // 업데이트 메서드 호출
//                repository.updateSchedule(schedule)
//            }
//        }
//    }
    fun updateSchedule(scheduleSeq: String, updatedScheduleData: Schedule) {
        viewModelScope.launch(Dispatchers.IO) { // 백그라운드 스레드에서 실행
            val existingSchedule = repository.getScheduleById(scheduleSeq)
            existingSchedule?.let { schedule ->
                // 업데이트할 속성 설정
                schedule.scheduleName = updatedScheduleData.scheduleName
                schedule.content = updatedScheduleData.content
                schedule.endDate = updatedScheduleData.endDate
                schedule.startDate = updatedScheduleData.startDate
                // 이미지 다시 뽑기가 필요하면 아래 주석을 풀어줍니다
                // schedule.wallpaperUrl = updatedScheduleData.wallpaperUrl

                // 업데이트 메서드 호출
                repository.updateSchedule(schedule)
            }
            // 필요하다면 결과를 메인 스레드로 보내는 코드를 추가
        }
    }

}
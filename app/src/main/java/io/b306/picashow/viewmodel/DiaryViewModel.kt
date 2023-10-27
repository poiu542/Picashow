package io.b306.picashow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.b306.picashow.entity.Diary
import io.b306.picashow.repository.DiaryRepository
import kotlinx.coroutines.launch

class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    // MutableLiveData 또는 StateFlow를 사용하여 UI에 변화를 알릴 수 있음
    private val _myInfo = MutableLiveData<Diary?>()
    val myInfo: LiveData<Diary?> get() = _myInfo

//    fun fetchMyInfo(id: Int) {
//        viewModelScope.launch {
//            val result: Diary? =
//                repository.fetchExerciseRecommendById(id).firstOrNull()
//            _myInfo.value = result
//        }
//    }

}
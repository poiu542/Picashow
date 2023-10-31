package io.b306.picashow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.b306.picashow.entity.Diary
import io.b306.picashow.repository.DiaryRepository
import kotlinx.coroutines.launch

class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {

    private val _myInfo = MutableLiveData<Diary?>()
    val myInfo: LiveData<Diary?> get() = _myInfo

    // Diary를 저장하는 함수
    fun saveDiary(diary: Diary) {
        viewModelScope.launch {
            // Diary를 저장하고 저장된 Diary 객체를 _myInfo LiveData에 할당
            val savedDiary = repository.insert(diary)
            _myInfo.value = diary
        }
    }

    // Diary를 업데이트하는 함수
    fun updateDiary(diary: Diary) {
        viewModelScope.launch {
            // Diary를 업데이트하고 업데이트된 Diary 객체를 _myInfo LiveData에 할당
            val updatedDiary = repository.update(diary)
            _myInfo.value = diary
        }
    }

    // Diary를 삭제하는 함수
    fun deleteDiary(diary: Diary) {
        viewModelScope.launch {
            // Diary를 삭제하고 LiveData를 null로 설정
            repository.delete(diary)
            _myInfo.value = null
        }
    }
}


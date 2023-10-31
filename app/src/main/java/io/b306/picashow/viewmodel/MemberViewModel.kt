package io.b306.picashow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.b306.picashow.entity.Diary
import io.b306.picashow.entity.Member
import io.b306.picashow.repository.DiaryRepository
import io.b306.picashow.repository.MemberRepository
import kotlinx.coroutines.launch

class MemberViewModel(private val repository: MemberRepository) : ViewModel() {
    private val _myInfo = MutableLiveData<Member?>()
    val myInfo: LiveData<Member?> get() = _myInfo
    fun saveMember(member: Member) {
        viewModelScope.launch {
            // Diary를 저장하고 저장된 Diary 객체를 _myInfo LiveData에 할당
            val savedDiary = repository.insert(member)
            _myInfo.value = member
        }
    }

    fun updateMember(member: Member) {
        viewModelScope.launch {
            // Diary를 업데이트하고 업데이트된 Diary 객체를 _myInfo LiveData에 할당
            val updatedDiary = repository.update(member)
            _myInfo.value = member
        }
    }

    // Diary를 삭제하는 함수
    fun deleteMember(member: Member) {
        viewModelScope.launch {
            // Diary를 삭제하고 LiveData를 null로 설정
            repository.delete(member)
            _myInfo.value = null
        }
    }

}
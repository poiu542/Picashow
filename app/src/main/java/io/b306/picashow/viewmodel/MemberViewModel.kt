package io.b306.picashow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.b306.picashow.entity.Diary
import io.b306.picashow.entity.Member
import io.b306.picashow.repository.DiaryRepository
import io.b306.picashow.repository.MemberRepository

class MemberViewModel(private val repository: MemberRepository) : ViewModel() {
    private val _myInfo = MutableLiveData<Member?>()
    val myInfo: LiveData<Member?> get() = _myInfo
}
package io.b306.picashow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.b306.picashow.entity.Member
import io.b306.picashow.repository.MemberRepository
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
val _myInfo = MutableLiveData<Member?>()
class MemberViewModel(private val repository: MemberRepository) : ViewModel() {

//    val myInfo: LiveData<Member?> get() = _myInfo

    // Function to get a specific member
    fun getMember(memberSeq: Long) {
        viewModelScope.launch {
            val member = repository.getMember(memberSeq).singleOrNull()
            _myInfo.value = member
        }
    }

    // Function to save a member
    fun saveMember(member: Member) {
        viewModelScope.launch {
            repository.insert(member)
            _myInfo.value = member
        }
    }

    // Function to update a member
    fun updateMember(member: Member) {
        viewModelScope.launch {
            repository.update(member)
            _myInfo.value = member
        }
    }

    // Function to delete a member
    fun deleteMember(member: Member) {
        viewModelScope.launch {
            repository.delete(member)
            _myInfo.value = null
        }
    }
}

package com.example.studentrollcall.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.User
import com.example.studentrollcall.repository.UserRepository
import java.lang.Exception

class UserViewModel: ViewModel(), UserRepository.OnFirestoreTaskComplete{
    private val repo = UserRepository(this)
    private val user = MutableLiveData<User>()

    init {
        repo.loadUserData()
    }

    override fun userDataLoaded(user: User) {
        this.user.value = user
    }

    override fun onError(e: Exception) {

    }
}
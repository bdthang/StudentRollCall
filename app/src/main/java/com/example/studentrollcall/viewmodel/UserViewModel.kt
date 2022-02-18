package com.example.studentrollcall.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.User
import com.example.studentrollcall.repository.UserRepository
import java.lang.Exception

class UserViewModel : ViewModel(), UserRepository.OnUserOperationComplete {
    private val repo = UserRepository(this)
    private val user = MutableLiveData<User>()
    private val successfulRegistration = MutableLiveData<Int>()

    fun createUser(newUser: User, email: String, pwd: String): MutableLiveData<Int> {
        successfulRegistration.value = -1
        repo.createUser(newUser, email, pwd)
        return successfulRegistration
    }

    fun getUserData(): MutableLiveData<User> {
        repo.loadUserData()
        return user
    }

    override fun userNotLogin() {
        this.user.value = null
    }

    override fun userCreated() {
        this.successfulRegistration.value = 0
    }

    override fun userCreationFailed() {
        this.successfulRegistration.value = 1
    }

    override fun userDataLoaded(user: User) {
        this.user.value = user
    }

    override fun onError(e: Exception) {

    }
}
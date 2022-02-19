package com.example.studentrollcall.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.User
import com.example.studentrollcall.repository.UserRepository
import java.lang.Exception

class UserViewModel : ViewModel(), UserRepository.OnUserOperationComplete {
    private val repo = UserRepository(this)
    private val user = MutableLiveData<User>()
    private val userStatus = MutableLiveData<Boolean>()
    private val loginOperationReturnCode = MutableLiveData<Int>()
    private val registerOperationReturnCode = MutableLiveData<Int>()

    fun createUser(newUser: User, email: String, pwd: String): MutableLiveData<Int> {
        registerOperationReturnCode.value = -1
        repo.createUser(newUser, email, pwd)
        return registerOperationReturnCode
    }

    fun login(email: String, pwd: String): MutableLiveData<Int> {
        loginOperationReturnCode.value = -1
        repo.login(email, pwd)
        return loginOperationReturnCode
    }

    fun logout(): MutableLiveData<Boolean> {
        repo.logout()
        return userStatus
    }

    fun getUserData(): MutableLiveData<User> {
        repo.loadUserData()
        return user
    }

    fun getUserStatus(): MutableLiveData<Boolean> {
        repo.loadUserStatus()
        return userStatus
    }

    override fun userNotLogin() {
        this.userStatus.value = false
    }

    override fun userCreationSuccessful() {
        this.userStatus.value = true
        this.registerOperationReturnCode.value = 0
    }

    override fun userCreationFailed() {
        this.userStatus.value = false
        this.registerOperationReturnCode.value = 1
    }

    override fun userLoginSuccessful() {
        this.userStatus.value = true
        this.loginOperationReturnCode.value = 0
    }

    override fun userLoginFailed() {
        this.userStatus.value = false
        this.loginOperationReturnCode.value = 1
    }

    override fun userDataLoaded(user: User) {
        this.user.value = user
    }

    override fun onError(e: Exception) {
    }

    override fun onLogout() {
        this.userStatus.value = false
    }

}
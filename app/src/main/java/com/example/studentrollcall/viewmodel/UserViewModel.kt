package com.example.studentrollcall.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.User
import com.example.studentrollcall.repository.UserRepository

class UserViewModel : ViewModel(), UserRepository.OnUserOperationComplete {
    private val repo = UserRepository(this)
    private val user = MutableLiveData<User>()
    private val userStatus = MutableLiveData<Boolean>()
    private val userId = MutableLiveData<String>()
    private val users = MutableLiveData<ArrayList<User>>()
    private val loginOperationReturnCode = MutableLiveData<Int>()
    private val registerOperationReturnCode = MutableLiveData<Int>()
    private val addClassOperationReturnCode = MutableLiveData<Int>()

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

    fun logout() {
        repo.logout()
    }

    fun getUserData(): MutableLiveData<User> {
        repo.loadUserData()
        return user
    }

    fun getUsers(_class: Class): MutableLiveData<ArrayList<User>> {
        repo.loadUsers(_class)
        return users
    }

    fun getUserId(): MutableLiveData<String> {
        repo.loadUserId()
        return userId
    }

    fun getUserStatus(): MutableLiveData<Boolean> {
        repo.loadUserStatus()
        return userStatus
    }

    fun studentAddClass(classShortId: String): MutableLiveData<Int> {
        addClassOperationReturnCode.value = -1
        repo.studentAddClass(classShortId)
        return addClassOperationReturnCode
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

    override fun userIdLoaded(userId: String) {
        this.userId.value = userId
    }

    override fun userDataLoaded(user: User) {
        this.user.value = user
    }

    override fun onError(e: Exception) {
    }

    override fun onLogout() {
        this.userStatus.value = false
    }

    override fun classAddedSuccessful() {
        this.addClassOperationReturnCode.value = 0
    }

    override fun classAddedFail() {
        this.addClassOperationReturnCode.value = 1
    }

    override fun onUsersLoaded(users: ArrayList<User>) {
        this.users.value = users
    }

}
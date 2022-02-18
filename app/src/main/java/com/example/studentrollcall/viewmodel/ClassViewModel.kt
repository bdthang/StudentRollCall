package com.example.studentrollcall.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.User
import com.example.studentrollcall.repository.ClassRepository

class ClassViewModel: ViewModel(), ClassRepository.OnFirestoreTaskComplete {
    private val repo = ClassRepository(this)
    private val classes = MutableLiveData<ArrayList<Class>>()

    fun getClassData(user: User): LiveData<ArrayList<Class>> {
        repo.loadClassData(user.classes)
        return classes
    }

    override fun classListDataAdded(classes: ArrayList<Class>) {
        this.classes.value = classes
    }

    override fun onError(e: Exception) {

    }
}
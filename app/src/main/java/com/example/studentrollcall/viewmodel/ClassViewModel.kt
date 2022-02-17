package com.example.studentrollcall.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.Class

class ClassViewModel: ViewModel(), ClassRepository.OnFirestoreTaskComplete {
    private val repo = ClassRepository(this)
    private val classes = MutableLiveData<ArrayList<Class>>()

    init {
        repo.loadClassData()
    }

    fun getClassData(): LiveData<ArrayList<Class>> {
        return classes
    }

    override fun classListDataAdded(classes: ArrayList<Class>) {
        this.classes.value = classes
    }

    override fun onError(e: Exception) {

    }
}
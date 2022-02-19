package com.example.studentrollcall.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.repository.ClassRepository

class ClassViewModel : ViewModel(), ClassRepository.OnFirestoreTaskComplete {
    private val TAG = "ClassViewModel"
    private val repo = ClassRepository(this)
    private val classes = MutableLiveData<ArrayList<Class>>()
    private val mapOfClasses = emptyMap<Int, ArrayList<Class>>().toMutableMap()
    private val classOperationReturnCode = MutableLiveData<Int>()

    fun getClassData(): LiveData<ArrayList<Class>> {
        repo.loadClassData()
        return classes
    }

    fun addClass(_class: Class): MutableLiveData<Int> {
        classOperationReturnCode.value = -1
        repo.addClass(_class)
        return classOperationReturnCode
    }

    fun updateClass(_class: Class, oldShortId: String): MutableLiveData<Int> {
        classOperationReturnCode.value = -1
        repo.updateClass(_class, oldShortId)
        return classOperationReturnCode
    }

    override fun emptyClassListData() {
        this.classes.value = ArrayList()
    }

    override fun onError(e: Exception) {

    }

    override fun classListDataUpdated(totalChunk:Int, chunkNum: Int, classes: ArrayList<Class>) {
        mapOfClasses[chunkNum] = classes

        if (mapOfClasses.size == totalChunk) {
            val list = ArrayList<Class>()
            mapOfClasses.forEach {
                list.addAll(it.value)
            }
            this.classes.value = list
        }
    }

    override fun classListDataCreated() {
        mapOfClasses.clear()
    }

    override fun classExisted() {
        this.classOperationReturnCode.value = 1
    }

    override fun classUpdatedSuccessfully() {
        this.classOperationReturnCode.value = 0
    }
}
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

    fun getClassData(): LiveData<ArrayList<Class>> {
        repo.loadClassData()
        return classes
    }

    override fun emptyClassListData() {
        this.classes.value = ArrayList()
    }

    override fun onError(e: Exception) {

    }

    override fun classListDataUpdated(totalChunk:Int, chunkNum: Int, classes: ArrayList<Class>) {
//        val originalClassList = ArrayList<Class>()
//        this.classes.value?.let { originalClassList.addAll(it) }

//        val array = arrayOfNulls<Class>(totalSize)
//        classes.toArray().forEach {
//            Log.d(TAG, it.toString())
//        }

        mapOfClasses[chunkNum] = classes

        if (mapOfClasses.size == totalChunk) {
            val list = ArrayList<Class>()
            mapOfClasses.forEach {
                list.addAll(it.value)
            }
            this.classes.value = list
        }


        // Fill array with class data
        // Convert list of classes to array first.

//        val modifiedClassList = ArrayList<Class>()
//        this.classes.value?.let { modifiedClassList.addAll(it) }
//        classes.forEachIndexed { index, _class ->
//            modifiedClassList[(chunkNum * 10) + index] = _class
//        }
//        this.classes.value = modifiedClassList
//        Log.d(TAG, this.classes.value.toString())
//        Log.d(TAG, modifiedClassList.toString())
    }

    override fun classListDataCreated() {
        mapOfClasses.clear()
    }
}
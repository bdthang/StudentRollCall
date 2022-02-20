package com.example.studentrollcall.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.Entry
import com.example.studentrollcall.repository.EntryRepository
import java.lang.Exception

class EntryViewModel() : ViewModel(), EntryRepository.OnFirestoreTaskComplete {
    private val repo = EntryRepository(this)
    private val entries = MutableLiveData<ArrayList<Entry>>()
    private val operationReturnCode = MutableLiveData<Int>()

    fun getEntries(_class: Class): MutableLiveData<ArrayList<Entry>> {
        repo.loadEntryData(_class.uid)
        return entries
    }

    fun addEntry(entry: Entry, photo: Bitmap): MutableLiveData<Int> {
        operationReturnCode.value = -1
        repo.addEntry(entry, photo)
        return operationReturnCode
    }

    override fun entryListDataLoaded(entries: ArrayList<Entry>) {
        this.entries.value = entries
    }

    override fun onError(e: Exception) {

    }

    override fun entryCreatedSuccessfully() {
        operationReturnCode.value = 0
    }

    override fun entryCreationFailed() {
        operationReturnCode.value = 1
    }

}
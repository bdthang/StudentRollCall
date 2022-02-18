package com.example.studentrollcall.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.Entry
import com.example.studentrollcall.repository.EntryRepository
import java.lang.Exception

class EntryViewModel() : ViewModel(), EntryRepository.OnFirestoreTaskComplete {
    private val repo = EntryRepository(this)
    private val entries = MutableLiveData<ArrayList<Entry>>()

    fun getEntries(_class: Class): MutableLiveData<ArrayList<Entry>> {
        repo.loadEntryData(_class.uid)
        return entries
    }

    override fun entryListDataLoaded(entries: ArrayList<Entry>) {
        this.entries.value = entries
    }

    override fun onError(e: Exception) {

    }

}
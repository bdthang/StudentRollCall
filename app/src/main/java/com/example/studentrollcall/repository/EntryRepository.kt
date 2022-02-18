package com.example.studentrollcall.repository

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.studentrollcall.model.Entry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class EntryRepository(private val onFirestoreTaskComplete: OnFirestoreTaskComplete) {
    private val TAG = "EntryRepository"
    private val db = FirebaseFirestore.getInstance()
    private val entryRef = db.collection("entries")

    fun loadEntryData(classId: String) {
        entryRef
            .whereEqualTo("classId", classId)
            .get()
            .addOnSuccessListener {
                onFirestoreTaskComplete.entryListDataLoaded(it.toObjects(Entry::class.java) as ArrayList<Entry>)
            }
            .addOnFailureListener {
                onFirestoreTaskComplete.onError(it)
            }
    }

    interface OnFirestoreTaskComplete {
        fun entryListDataLoaded(entries: ArrayList<Entry>)
        fun onError(e: Exception)
    }
}
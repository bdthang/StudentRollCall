package com.example.studentrollcall.repository

import com.example.studentrollcall.model.Class
import com.google.firebase.firestore.FirebaseFirestore

class ClassRepository(private val onFirestoreTaskComplete: OnFirestoreTaskComplete) {
    private val TAG = "ClassRepository"
    private val db = FirebaseFirestore.getInstance()
    private val classRef = db.collection("classes")

    fun loadClassData(classes: ArrayList<String>) {
        classRef.whereIn("__name__", classes)
            .addSnapshotListener { value, error ->
            if (error != null) {
                onFirestoreTaskComplete.onError(error)
                return@addSnapshotListener
            }

            onFirestoreTaskComplete.classListDataAdded(value!!.toObjects(Class::class.java) as ArrayList<Class>)
        }
    }

    interface OnFirestoreTaskComplete {
        fun classListDataAdded(classes: ArrayList<Class>)
        fun onError(e: Exception)
    }
}
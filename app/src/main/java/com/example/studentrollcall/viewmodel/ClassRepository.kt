package com.example.studentrollcall.viewmodel

import android.util.Log
import com.example.studentrollcall.model.Class
import com.google.firebase.firestore.FirebaseFirestore

class ClassRepository(private val onFirestoreTaskComplete: OnFirestoreTaskComplete) {
    private val TAG = "ClassRepository"
    private val db = FirebaseFirestore.getInstance()
    private val classRef = db.collection("classes")

    fun loadClassData() {
//        classRef.get()
//            .addOnSuccessListener {
//                onFirestoreTaskComplete.classListDataAdded(it.toObjects(Class::class.java) as ArrayList<Class>)
//            }
//            .addOnFailureListener {
//                onFirestoreTaskComplete.onError(it)
//            }
        classRef.addSnapshotListener { value, error ->
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
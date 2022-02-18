package com.example.studentrollcall.repository

import com.example.studentrollcall.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class UserRepository(private val onFirestoreTaskComplete: OnFirestoreTaskComplete) {
    private val db = FirebaseFirestore.getInstance()
    private val userRef = db.collection("users")

    fun loadUserData() {
        TODO("Implement auth first, then do this")
    }

    interface OnFirestoreTaskComplete {
        fun userDataLoaded(user: User)
        fun onError(e: Exception)
    }
}
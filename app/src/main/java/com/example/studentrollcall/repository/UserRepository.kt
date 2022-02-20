package com.example.studentrollcall.repository

import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class UserRepository(private val onUserOperationComplete: OnUserOperationComplete) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userRef = db.collection("users")
    private val classRef = db.collection("classes")

    fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onUserOperationComplete.userNotLogin()
            return
        }

        userRef.document(currentUser.uid).get()
            .addOnSuccessListener {
                if (it != null) {
                    onUserOperationComplete.userDataLoaded(it.toObject(User::class.java)!!)
                }
            }
            .addOnFailureListener {
                onUserOperationComplete.onError(it)
            }
    }

    fun createUser(user: User, email: String, pwd: String) {
        auth.createUserWithEmailAndPassword(email, pwd)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    userRef.document(currentUser.uid).set(user)
                        .addOnSuccessListener {
                            onUserOperationComplete.userCreationSuccessful()
                        }
                }

            }
            .addOnFailureListener {
                onUserOperationComplete.userCreationFailed()
            }
    }

    fun login(email: String, pwd: String) {
        auth.signInWithEmailAndPassword(email, pwd)
            .addOnSuccessListener {
                onUserOperationComplete.userLoginSuccessful()
            }
            .addOnFailureListener {
                onUserOperationComplete.userLoginFailed()
            }
    }

    fun logout() {
        auth.signOut()
        onUserOperationComplete.onLogout()
    }

    fun loadUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            onUserOperationComplete.userLoginSuccessful()
        } else {
            onUserOperationComplete.userLoginFailed()
        }
    }

    fun studentAddClass(classShortId: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userRef.document(currentUser.uid).get().addOnSuccessListener {
                if (it != null) {
                    val user: User = it.toObject(User::class.java)!!

                    classRef.whereEqualTo("shortId", classShortId)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { classesQuery ->
                            if (classesQuery.isEmpty) {
                                // No such class
                                onUserOperationComplete.classAddedFail()
                            } else {
                                val classes = classesQuery.toObjects(Class::class.java)
                                val _class = classes[0]

                                if (!user.classes.contains(_class.uid)) {
                                    user.classes.add(_class.uid)
                                    userRef.document(currentUser.uid).set(user)
                                }
                                onUserOperationComplete.classAddedSuccessful()
                            }

                        }
                }

            }
        }
    }

    fun loadUserId() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            onUserOperationComplete.userIdLoaded(currentUser.uid)
        }
    }

    interface OnUserOperationComplete {
        fun userNotLogin()
        fun userCreationSuccessful()
        fun userCreationFailed()
        fun userLoginSuccessful()
        fun userLoginFailed()
        fun userIdLoaded(userId: String)
        fun userDataLoaded(user: User)
        fun onError(e: Exception)
        fun onLogout()
        fun classAddedSuccessful()
        fun classAddedFail()
    }
}
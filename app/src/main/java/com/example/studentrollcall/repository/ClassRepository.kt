package com.example.studentrollcall.repository

import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ClassRepository(private val onFirestoreTaskComplete: OnFirestoreTaskComplete) {
    private val TAG = "ClassRepository"
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val classRef = db.collection("classes")
    private val userRef = db.collection("users")

    fun loadClassData() {
        if (auth.currentUser == null) {
            onFirestoreTaskComplete.emptyClassListData()
            return
        }

        userRef.document(auth.currentUser!!.uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFirestoreTaskComplete.onError(error)
                } else {
                    if (value != null){
                        onFirestoreTaskComplete.classListDataCreated()

                        val user: User = value.toObject(User::class.java) as User
                        val classes = user.classes
                        if (classes.isNotEmpty()) {
                            val classChunked = classes.chunked(10)

                            classChunked.forEachIndexed { index, it ->
                                classRef.whereIn("__name__", it)
                                    .addSnapshotListener { value, error ->
                                        if (error != null) {
                                            onFirestoreTaskComplete.onError(error)
                                        } else {
                                            onFirestoreTaskComplete.classListDataUpdated(classChunked.size, index, value!!.toObjects(Class::class.java) as ArrayList<Class>)
                                        }
                                    }
                            }

                        } else {
                            onFirestoreTaskComplete.emptyClassListData()
                        }

                    }

                }

            }


    }

    interface OnFirestoreTaskComplete {
        fun emptyClassListData()
        fun onError(e: Exception)
        fun classListDataUpdated(totalChunk: Int, chunkNum: Int, classes: ArrayList<Class>)
        fun classListDataCreated()
    }
}
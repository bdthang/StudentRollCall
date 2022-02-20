package com.example.studentrollcall.repository

import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ClassRepository(private val onFirestoreTaskComplete: OnFirestoreTaskComplete) {
    private val TAG = "ClassRepository"
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val classRef = db.collection("classes")
    private val userRef = db.collection("users")

    fun loadClassData() {
        userRef.document(auth.currentUser!!.uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFirestoreTaskComplete.onError(error)
                } else {
                    if (value != null) {
                        onFirestoreTaskComplete.classListDataCreated()

                        val user: User = value.toObject(User::class.java) as User

                        // If user is student
                        if (!user.teacher) {
                            val classes = user.classes
                            if (classes.isNotEmpty()) {
                                val classChunked = classes.chunked(10)

                                classChunked.forEachIndexed { index, it ->
                                    classRef.whereIn("__name__", it)
                                        .addSnapshotListener { value, error ->
                                            if (error != null) {
                                                onFirestoreTaskComplete.onError(error)
                                            } else {
                                                onFirestoreTaskComplete.classListDataUpdated(
                                                    classChunked.size,
                                                    index,
                                                    value!!.toObjects(Class::class.java) as ArrayList<Class>
                                                )
                                            }
                                        }
                                }

                            } else {
                                onFirestoreTaskComplete.emptyClassListData()
                            }
                        } else if (user.teacher) {
                            classRef.whereEqualTo("authorId", user.uid)
                                .addSnapshotListener { value2, error2 ->
                                    if (error2 != null) {
                                        onFirestoreTaskComplete.onError(error2)
                                    } else {
                                        onFirestoreTaskComplete.classListDataUpdated(
                                            1,
                                            0,
                                            value2!!.toObjects(Class::class.java) as ArrayList<Class>
                                        )
                                    }
                                }
                        }


                    }

                }

            }


    }

    fun addClass(_class: Class) {
        _class.authorId = auth.currentUser!!.uid

        classRef.whereEqualTo("shortId", _class.shortId).get()
            .addOnSuccessListener {
                if (it.isEmpty) { // If short id not exist yet, add
                    classRef.add(_class)
                    onFirestoreTaskComplete.classUpdatedSuccessfully()
                } else {
                    onFirestoreTaskComplete.classExisted()
                }
            }
    }

    fun updateClass(_class: Class, oldShortId: String) {
        classRef.whereEqualTo("shortId", _class.shortId).get()
            .addOnSuccessListener {
                // If short id not exist yet or same, update
                if (it.isEmpty) {
                    classRef.document(_class.uid).set(_class)
                    onFirestoreTaskComplete.classUpdatedSuccessfully()
                } else if (it.size() == 1) {
                    val classes: ArrayList<Class> = it.toObjects(Class::class.java) as ArrayList<Class>
                    if (classes[0].shortId == oldShortId) {
                        classRef.document(_class.uid).set(_class)
                        onFirestoreTaskComplete.classUpdatedSuccessfully()
                    } else {
                        onFirestoreTaskComplete.classExisted()
                    }
                } else {
                    onFirestoreTaskComplete.classExisted()
                }
            }
    }

    fun deleteClass(_class: Class) {
        classRef.document(_class.uid).delete()
            .addOnSuccessListener {
                onFirestoreTaskComplete.classUpdatedSuccessfully()
            }
            .addOnFailureListener {
                onFirestoreTaskComplete.onError(it)
            }
    }

    fun createNewSession(_class: Class) {
        classRef.document(_class.uid).update(
            "currentSession", _class.currentSession + 1,
        "timeStart", FieldValue.serverTimestamp())
            .addOnSuccessListener {
                onFirestoreTaskComplete.classUpdatedSuccessfully()
            }
            .addOnFailureListener {
                onFirestoreTaskComplete.onError(it)
            }
    }

    interface OnFirestoreTaskComplete {
        fun emptyClassListData()
        fun onError(e: Exception)
        fun classListDataUpdated(totalChunk: Int, chunkNum: Int, classes: ArrayList<Class>)
        fun classListDataCreated()
        fun classExisted()
        fun classUpdatedSuccessfully()
    }
}
package com.example.studentrollcall.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

data class User(
    @Exclude
    @DocumentId
    val uid: String = "",
    val name: String = "user_name",
    val classes: ArrayList<String> = ArrayList()
)

package com.example.studentrollcall.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import java.util.*
import kotlin.collections.ArrayList

data class User(
    @Exclude
    @DocumentId
    val uid: String = "",
    val name: String = "user_name",
    val teacher: Boolean = false,
    val studentId: Int = 0,
    val classes: ArrayList<String> = ArrayList(),
)

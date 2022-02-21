package com.example.studentrollcall.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class User(
    @Exclude
    @DocumentId
    val uid: String = "",
    val name: String = "user_name",
    val teacher: Boolean = false,
    val studentId: Int = 0,
    val classes: ArrayList<String> = ArrayList(),
    val email: String = ""
) : Parcelable

package com.example.studentrollcall.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FieldValue
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class Class(
    @Exclude
    @DocumentId
    val uid: String = "",
    val title: String = "Name",
    val shortId: String = "",
    val description: String = "Description",
    val timeLimit: Int = 30,
    val currentSession: Int = 0,
    var authorId: String = "",
    val timeStart: Date = Date(0),
    ) : Parcelable

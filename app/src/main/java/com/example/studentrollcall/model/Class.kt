package com.example.studentrollcall.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Class(
    @Exclude
    @DocumentId
    val uid: String = "",
    val title: String = "",
    val shortId: String = "",
    val currentSession: Int = 1
) : Parcelable

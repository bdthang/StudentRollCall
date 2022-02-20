package com.example.studentrollcall.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Entry(
    val userId: String = "",
    val classId: String = "",
    val session: Int = 1
) : Parcelable

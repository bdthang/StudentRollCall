package com.example.studentrollcall.helper

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.Patterns
import com.example.studentrollcall.App
import com.example.studentrollcall.R

fun validEmail(email: String): String? {
    if (email.isEmpty()) {
        return App.instance.getString(R.string.required)
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return App.instance.getString(R.string.invalid_email)
    }

    return null
}

fun validPassword(password: String): String? {
    if (password.length < 6) {
        return App.instance.getString(R.string.short_password)
    }

    return null
}

fun validPasswordConfirmation(pwd: String, otherPwd: String): String? {
    if (pwd.length < 6) {
        return App.instance.getString(R.string.short_password)
    }
    if (pwd != otherPwd) {
        return App.instance.getString(R.string.failed_password_confirmation)
    }
    return null
}

fun validName(name: String): String? {
    if (name.isBlank()) {
        return App.instance.getString(R.string.required)
    }
    return null
}

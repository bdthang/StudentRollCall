package com.example.studentrollcall.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.Entry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class EntryRepository(private val onFirestoreTaskComplete: OnFirestoreTaskComplete) {
    private val TAG = "EntryRepository"
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private val entryRef = db.collection("entries")

    fun loadEntryData(classId: String) {
        entryRef
            .whereEqualTo("classId", classId)
            .get()
            .addOnSuccessListener {
                onFirestoreTaskComplete.entryListDataLoaded(it.toObjects(Entry::class.java) as ArrayList<Entry>)
            }
            .addOnFailureListener {
                onFirestoreTaskComplete.onError(it)
            }
    }

    fun addEntry(entry: Entry, photo: Bitmap) {
        // Check for duplicate
        entryRef.whereEqualTo("userId", entry.userId)
            .whereEqualTo("classId", entry.classId)
            .whereEqualTo("session", entry.session)
            .get()
            .addOnSuccessListener { it2 ->
                if (it2.isEmpty) {
                    entryRef.add(entry).addOnSuccessListener {
                        onFirestoreTaskComplete.entryCreatedSuccessfully()
                    }.addOnFailureListener {
                        onFirestoreTaskComplete.entryCreationFailed()
                    }
                } else {
                    onFirestoreTaskComplete.entryCreatedSuccessfully()
                }
                val imgPath = entry.userId + "/" + entry.classId + "/" + entry.session + ".jpg"
                saveImage(photo, imgPath)
            }
            .addOnFailureListener { e ->
                onFirestoreTaskComplete.onError(e)
            }

    }

    private fun saveImage(photo: Bitmap, imgPath: String) {
        val imgRef = storageRef.child(imgPath)
        val baos = ByteArrayOutputStream()

        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        imgRef.putBytes(data).addOnFailureListener {
            Log.d(TAG, "Upload image error $it")
        }
    }

    interface OnFirestoreTaskComplete {
        fun entryListDataLoaded(entries: ArrayList<Entry>)
        fun onError(e: Exception)
        fun entryCreatedSuccessfully()
        fun entryCreationFailed()
    }
}
package com.example.studentrollcall.fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.studentrollcall.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PhotoFragment : Fragment(R.layout.fragment_photo) {
    private val args: PhotoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entry = args.entryToShow
        val imgUrl = entry.userId + "/" + entry.classId + "/" + entry.session + ".jpg"
        val imgView: ImageView = view.findViewById(R.id.img_photo)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        FirebaseStorage.getInstance()
            .reference.child(imgUrl)
            .downloadUrl
            .addOnSuccessListener {
                val width = Resources.getSystem().displayMetrics.widthPixels
                Picasso.get().load(it.toString())
                    .resize(width, 0)
                    .into(imgView, object: Callback {
                        override fun onSuccess() {
                            progressBar.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            progressBar.visibility = View.GONE
                            Snackbar.make(requireView(), getString(R.string.image_loading_failed), Snackbar.LENGTH_SHORT).show()
                        }

                    })
            }
    }
}
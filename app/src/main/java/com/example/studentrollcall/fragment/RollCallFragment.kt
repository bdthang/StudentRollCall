package com.example.studentrollcall.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentRollCallBinding
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.Entry
import com.example.studentrollcall.viewmodel.EntryViewModel
import com.example.studentrollcall.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class RollCallFragment: Fragment(R.layout.fragment_roll_call) {
    private var _binding: FragmentRollCallBinding? = null
    private val binding get() = _binding!!
    private val CAMERA_REQUEST_CODE = 101
    private var photo: Bitmap? = null
    private var studentId: Int = 0
    private val args: RollCallFragmentArgs by navArgs()
    private val entryViewModel: EntryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRollCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _class = args.classToRollCall

        binding.apply {
            tvClass.text = _class.title
            imgViewPhotoResult.setImageBitmap(scaledImg(photo))

            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("studentId")
                ?.observe(viewLifecycleOwner) { result ->
                    tvStudentIdResult.text = result.toString()
                    studentId = result
                }

            buttonScanId.setOnClickListener {
                val action = RollCallFragmentDirections.actionRollCallFragmentToScannerFragment()
                findNavController().navigate(action)
            }

            buttonTakePhoto.setOnClickListener {
                askForCamera()
            }

            buttonSubmit.setOnClickListener {
                addEntry(_class)
            }
        }
    }

    private fun addEntry(_class: Class) {
        if (studentId == 0 || photo == null) {
            Snackbar.make(requireView(), getString(R.string.missing_info), Snackbar.LENGTH_SHORT).show()
            return
        }

        userViewModel.getUserData().observe(viewLifecycleOwner) { user ->
            val userId = user.uid
            val classId = _class.uid
            val session = _class.currentSession

                if (studentId == user.studentId) {
                    val entry = Entry(userId, classId, session)
                    entryViewModel.addEntry(entry, photo!!).observe(viewLifecycleOwner) {
                        if (it == 0) {
                            Snackbar.make(requireView(), getString(R.string.roll_call_successful), Snackbar.LENGTH_SHORT).show()
                        } else if (it == 1) {
                            Snackbar.make(requireView(), getString(R.string.roll_call_failed), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Snackbar.make(requireView(), getString(R.string.wrong_student_id), Snackbar.LENGTH_SHORT).show()
                }
            }

    }

    private fun askForCamera() {
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            openCamera()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(requireView(), getString(R.string.camera_request_message), Snackbar.LENGTH_SHORT).show()
                } else {
                    openCamera()
                }
            }
        }
    }

    private fun openCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camera, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            try {
                val originalPhoto = data!!.extras!!.get("data") as Bitmap

                // Mirror image
//                val matrix = Matrix()
//                matrix.setScale(-1F, 1F)
//                matrix.postTranslate(originalPhoto.width.toFloat(), 0F)
//                photo = Bitmap.createBitmap(originalPhoto, 0, 0, originalPhoto.width, originalPhoto.height, matrix, true)

                photo = originalPhoto
                binding.imgViewPhotoResult.setImageBitmap(scaledImg(originalPhoto))
            } catch (e: NullPointerException) {
                Log.e("Camera", "Photo not retrieved. $e")
            }
        }
    }

    fun scaledImg(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) {
            return null
        }
        // Scale image to fit
        val width = Resources.getSystem().displayMetrics.widthPixels
        return bitmap.scale(width, width / bitmap.width * bitmap.height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.studentrollcall.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studentrollcall.helper.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentAddEditClassBinding
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.viewmodel.ClassViewModel
import com.google.android.material.snackbar.Snackbar

class AddEditClassFragment : Fragment(R.layout.fragment_add_edit_class) {

    private var _binding: FragmentAddEditClassBinding? = null
    private val binding get() = _binding!!
    private val args: AddEditClassFragmentArgs by navArgs()
    private val classViewModel: ClassViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etTitle.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val title = etTitle.text.toString().trim()
                    containerTitle.helperText = validNotBlank(title)
                }
            }

            etTimeLimit.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val timeLimit = etTimeLimit.text.toString().trim()
                    containerTimeLimit.helperText = validNotBlank(timeLimit)
                }
            }

            etShortId.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val shortId = etShortId.text.toString().trim()
                    containerShortId.helperText = validNotBlank(shortId)
                }
            }

            etDescription.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val desc = etDescription.text.toString().trim()
                    containerDescription.helperText = validNotBlank(desc)
                }
            }
        }

        val classToEdit = args.classToEdit
        if (classToEdit != null) {
            binding.apply {
                etTitle.setText(classToEdit.title)
                etDescription.setText(classToEdit.description)
                etShortId.setText(classToEdit.shortId)
                etTimeLimit.setText(classToEdit.timeLimit.toString())

                buttonConfirmClass.setOnClickListener {
                    if (validateField()) {
                        val title = etTitle.text.toString().trim()
                        val timeLimit = etTimeLimit.text.toString().trim().toInt()
                        val shortId = etShortId.text.toString().trim()
                        val desc = etDescription.text.toString().trim()

                        val _class = Class(classToEdit.uid, title, shortId, desc, timeLimit, classToEdit.currentSession, classToEdit.authorId)
                        classViewModel.updateClass(_class, classToEdit.shortId).observe(viewLifecycleOwner) {
                            if (it == 0) {
                                findNavController().navigateUp()
                            } else if (it == 1) {
                                Snackbar.make(requireView(), getString(R.string.class_existed), Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        } else {
            binding.apply {
                buttonConfirmClass.setOnClickListener {
                    if (validateField()) {
                        val title = etTitle.text.toString().trim()
                        val timeLimit = etTimeLimit.text.toString().trim().toInt()
                        val shortId = etShortId.text.toString().trim()
                        val desc = etDescription.text.toString().trim()

                        val _class = Class("", title, shortId, desc, timeLimit)
                        classViewModel.addClass(_class).observe(viewLifecycleOwner) {
                            if (it == 0) {
                                findNavController().navigateUp()
                            } else if (it == 1) {
                                Snackbar.make(requireView(), getString(R.string.class_existed), Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validateField(): Boolean {
        binding.apply {
            val title = etTitle.text.toString().trim()
            containerTitle.helperText = validNotBlank(title)
            val timeLimit = etTimeLimit.text.toString().trim()
            containerTimeLimit.helperText = validNotBlank(timeLimit)
            val shortId = etShortId.text.toString().trim()
            containerShortId.helperText = validNotBlank(shortId)
            val desc = etDescription.text.toString().trim()
            containerDescription.helperText = validNotBlank(desc)

            val validTitle = containerTitle.helperText == null
            val validDesc = containerDescription.helperText == null
            val validTimeLimit = containerTimeLimit.helperText == null
            val validShortId = containerShortId.helperText == null

            return validTitle && validDesc && validTimeLimit && validShortId
        }


    }
}
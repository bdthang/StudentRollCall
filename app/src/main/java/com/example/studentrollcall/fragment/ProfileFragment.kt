package com.example.studentrollcall.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentProfileBinding
import com.example.studentrollcall.helper.validNotBlank
import com.example.studentrollcall.model.User
import com.example.studentrollcall.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            userViewModel.getUserData().observe(viewLifecycleOwner) { user ->
                etName.setText(user.name)
                if (!user.teacher) {
                    etStudentId.setText(user.studentId.toString())

                    etStudentId.setOnFocusChangeListener { _, focused ->
                        if (!focused) {
                            val studentId = etStudentId.text.toString().trim()
                            containerMssv.helperText = validNotBlank(studentId)
                        }
                    }
                } else {
                    containerMssv.visibility = View.GONE
                }

                buttonConfirm.setOnClickListener {
                    val name = etName.text.toString().trim()
                    nameContainer.helperText = validNotBlank(name)
                    val validName = nameContainer.helperText == null

                    if (!user.teacher) {
                        val studentId = etStudentId.text.toString().trim()
                        containerMssv.helperText = validNotBlank(studentId)
                        val validStudentId = containerMssv.helperText == null

                        if (validName && validStudentId) {
                            val updatedUser = User("", name, user.teacher, studentId.toInt(), user.classes, user.email)
                            userViewModel.updateUser(updatedUser).observe(viewLifecycleOwner) {
                                if (it == 0) {
                                    Snackbar.make(requireView(), getString(R.string.profile_update_success), Snackbar.LENGTH_SHORT).show()
                                } else if(it == 1) {
                                    Snackbar.make(requireView(), getString(R.string.profile_update_failed), Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        if (validName) {
                            val updatedUser = User("", name, user.teacher, user.studentId, user.classes, user.email)
                            userViewModel.updateUser(updatedUser).observe(viewLifecycleOwner) {
                                if (it == 0) {
                                    Snackbar.make(requireView(), getString(R.string.profile_update_success), Snackbar.LENGTH_SHORT).show()
                                } else if(it == 1) {
                                    Snackbar.make(requireView(), getString(R.string.profile_update_failed), Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                }


            }

            etName.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val name = etName.text.toString().trim()
                    nameContainer.helperText = validNotBlank(name)
                }
            }
        }

    }
}
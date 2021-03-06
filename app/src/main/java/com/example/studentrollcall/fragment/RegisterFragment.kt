package com.example.studentrollcall.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentRegisterBinding
import com.example.studentrollcall.helper.validEmail
import com.example.studentrollcall.helper.validNotBlank
import com.example.studentrollcall.helper.validPassword
import com.example.studentrollcall.helper.validPasswordConfirmation
import com.example.studentrollcall.model.User
import com.example.studentrollcall.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private val TAG = "RegisterFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etName.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val name = etName.text.toString().trim()
                    nameContainer.helperText = validNotBlank(name)
                }
            }

            etEmail.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val email = etEmail.text.toString().trim()
                    emailContainer.helperText = validEmail(email)
                }
            }

            etPassword.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val pwd = etPassword.text.toString()
                    passwordContainer.helperText = validPassword(pwd)
                }
            }

            etPasswordConfirmation.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val pwdConfirm = etPasswordConfirmation.text.toString()
                    val pwd = etPassword.text.toString()
                    passwordConfirmationContainer.helperText = validPasswordConfirmation(pwdConfirm, pwd)
                }
            }

            etStudentId.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val studentId = etStudentId.text.toString().trim()
                    containerMssv.helperText = validNotBlank(studentId)
                }
            }

            val roles = resources.getStringArray(R.array.role)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, roles)
            atvRole.setAdapter(arrayAdapter)
            atvRole.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString() == getString(R.string.teacher)) {
                        containerMssv.visibility = View.GONE
                    } else {
                        containerMssv.visibility = View.VISIBLE
                    }
                }
            })

            buttonRegister.setOnClickListener {
                register()
            }
        }
    }

    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        binding.emailContainer.helperText = validEmail(email)
        val pwd = binding.etPassword.text.toString()
        binding.passwordContainer.helperText = validPassword(pwd)
        val pwdConfirm = binding.etPasswordConfirmation.text.toString()
        binding.passwordConfirmationContainer.helperText = validPasswordConfirmation(pwdConfirm, pwd)
        val name = binding.etName.text.toString().trim()
        binding.nameContainer.helperText = validNotBlank(name)
        val role = binding.atvRole.text.toString().trim() == resources.getString(R.string.teacher)
        var studentId = binding.etStudentId.text.toString().trim()
        if (studentId.isEmpty()) {studentId = "0"}
//        binding.containerMssv.helperText = validNotBlank(studentId)

        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validConfirmationPassword = binding.passwordConfirmationContainer.helperText == null
        val validName = binding.nameContainer.helperText == null
        val validStudentId = binding.containerMssv.helperText == null

        if (validEmail && validPassword && validConfirmationPassword && validName && validStudentId) {
            val newUser = User("", name, role, studentId.toInt(), ArrayList(), email)
            userViewModel.createUser(newUser, email, pwd).observe(viewLifecycleOwner) { result ->
                if (result == 0) {
                    Snackbar.make(requireView(), getString(R.string.successful_registration), Snackbar.LENGTH_SHORT).show()
                    val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                    findNavController().navigate(action)
                } else if (result == 1) {
                    Snackbar.make(requireView(), getString(R.string.failed_registration), Snackbar.LENGTH_SHORT).show()
                }
            }
        } else {
            Snackbar.make(requireView(), getString(R.string.wrong_information), Snackbar.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.studentrollcall.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentLoginBinding
import com.example.studentrollcall.helper.*
import com.google.android.material.snackbar.Snackbar

class LoginFragment: Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
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

            buttonLogin.setOnClickListener {
                login()
            }

            buttonRegister.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            }

            buttonForgotPassword.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRecoverPasswordFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        binding.emailContainer.helperText = validEmail(email)
        val pwd = binding.etPassword.text.toString()
        binding.passwordContainer.helperText = validPassword(pwd)

        val validEmail = binding.emailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null

        if (validEmail && validPassword) {
            // Login
            TODO("Login")
        } else {
            Snackbar.make(requireView(), getString(R.string.wrong_information), Snackbar.LENGTH_SHORT).show()
        }
    }
}
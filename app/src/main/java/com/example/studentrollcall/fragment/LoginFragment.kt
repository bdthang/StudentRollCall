package com.example.studentrollcall.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentLoginBinding
import com.example.studentrollcall.helper.*
import com.example.studentrollcall.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val TAG = "LoginFragment"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        userViewModel.getUserStatus().observe(viewLifecycleOwner) { userStatus ->
            if (userStatus) {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }

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
            userViewModel.login(email, pwd).observe(viewLifecycleOwner) { result ->
                if (result == 1) {
                    Snackbar.make(requireView(), getString(R.string.login_failed), Snackbar.LENGTH_SHORT).show()
                } else if (result == 0) {
                    Snackbar.make(requireView(), getString(R.string.successful_login), Snackbar.LENGTH_SHORT).show()
                }
            }
        } else {
            Snackbar.make(requireView(), getString(R.string.wrong_information), Snackbar.LENGTH_SHORT).show()
        }
    }
}
package com.example.studentrollcall.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentRecoverPasswordBinding
import com.example.studentrollcall.helper.validEmail
import com.example.studentrollcall.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class RecoverPasswordFragment: Fragment(R.layout.fragment_recover_password) {

    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etEmail.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    val email = etEmail.text.toString().trim()
                    containerEmail.helperText = validEmail(email)
                }
            }

            buttonRecoverPassword.setOnClickListener {
                val email = etEmail.text.toString().trim()
                containerEmail.helperText = validEmail(email)

                val validEmail = containerEmail.helperText == null
                if (validEmail) {
                    userViewModel.recoverPassword(email).observe(viewLifecycleOwner) {
                        if (it == 1) {
                            Snackbar.make(view, getString(R.string.invalid_email),Snackbar.LENGTH_SHORT).show()
                        } else if (it == 0){
                            Snackbar.make(view, getString(R.string.email_recover_sent),Snackbar.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Snackbar.make(view, getString(R.string.invalid_email),Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
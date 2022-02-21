package com.example.studentrollcall.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentProfileReadBinding

class ProfileReadFragment: Fragment(R.layout.fragment_profile_read) {
    private var _binding: FragmentProfileReadBinding? = null
    private val binding get() = _binding!!
    private val args: ProfileReadFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = args.userToShow
        binding.apply {
            tvName.text = user.name
            tvEmail.text = user.email
            tvStudentId.text = user.studentId.toString()
        }
    }
}
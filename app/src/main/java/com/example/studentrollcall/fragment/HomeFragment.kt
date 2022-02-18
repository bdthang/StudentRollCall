package com.example.studentrollcall.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studentrollcall.R
import com.example.studentrollcall.adapter.ClassAdapter
import com.example.studentrollcall.databinding.FragmentHomeBinding
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.User
import com.example.studentrollcall.viewmodel.ClassViewModel
import com.example.studentrollcall.viewmodel.UserViewModel

class HomeFragment: Fragment(R.layout.fragment_home), ClassAdapter.OnItemClickListener {
    private val TAG = "HomeFragment"
    private lateinit var classAdapter: ClassAdapter
    private val classViewModel: ClassViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getUserData().observe(viewLifecycleOwner) { user ->
            if (user == null) {
                val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                findNavController().navigate(action)
            } else {
                setupRecyclerView(user)
            }
        }
    }

    private fun setupRecyclerView(user: User) {
        classAdapter = ClassAdapter(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = classAdapter

        classViewModel.getClassData(user).observe(viewLifecycleOwner) {
            classAdapter.submitList(it)
//            Log.d(TAG, it.toString())
        }
    }

    override fun onItemClick(_class: Class) {
        val action = HomeFragmentDirections.actionHomeFragmentToClassFragment(_class)
        findNavController().navigate(action)
    }
}
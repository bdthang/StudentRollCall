package com.example.studentrollcall.fragment

import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studentrollcall.R
import com.example.studentrollcall.adapter.ClassAdapter
import com.example.studentrollcall.databinding.FragmentHomeBinding
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.viewmodel.ClassViewModel
import com.example.studentrollcall.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(R.layout.fragment_home), ClassAdapter.OnItemClickListener {
    private val TAG = "HomeFragment"
    private lateinit var classAdapter: ClassAdapter
    private val classViewModel: ClassViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        setupRecyclerView()

        userViewModel.getUserData().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                if (user.teacher) {
                    binding.fabAddClass.setOnClickListener {
                        val action = HomeFragmentDirections.actionHomeFragmentToAddEditClassFragment(null)
                        findNavController().navigate(action)
                    }
                } else {
                    binding.fabAddClass.setOnClickListener {
                        studentAddClass()
                    }
                }

            }
        }
    }

    private fun studentAddClass() {
        val etClassShortId = EditText(requireContext())
        etClassShortId.inputType = InputType.TYPE_CLASS_TEXT
        etClassShortId.visibility = View.VISIBLE
        etClassShortId.setSingleLine()

        val container = FrameLayout(requireContext())
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(32,0,32,0)
        etClassShortId.layoutParams = params
        container.addView(etClassShortId)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_class))
            .setMessage(getString(R.string.add_class_id))
            .setView(container)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                val classId = etClassShortId.text.toString().trim()
                if (classId.isNotEmpty()) {
                    userViewModel.studentAddClass(etClassShortId.text.toString().trim()).observe(viewLifecycleOwner) {
                        if (it == 1) {
                            Snackbar.make(requireView(), getString(R.string.no_class) + " " + classId, Snackbar.LENGTH_SHORT).show()
                        } else if (it == 0) {
                            Snackbar.make(requireView(), getString(R.string.class_added), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .show()
    }

    private fun setupRecyclerView() {
        classAdapter = ClassAdapter(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = classAdapter

        classViewModel.getClassData().observe(viewLifecycleOwner) {
            classAdapter.submitList(it)
//            Log.d(TAG, it.toString())
        }
    }

    override fun onItemClick(_class: Class) {
        val action = HomeFragmentDirections.actionHomeFragmentToClassFragment(_class)
        findNavController().navigate(action)
    }

    override fun onItemOptionClick(_class: Class) {
        val action = HomeFragmentDirections.actionHomeFragmentToAddEditClassFragment(_class)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_logout) {
            userViewModel.logout()
            findNavController().navigateUp()
            return true
        } else if (item.itemId == R.id.option_profile) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
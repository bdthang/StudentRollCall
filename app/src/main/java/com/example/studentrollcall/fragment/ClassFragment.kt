package com.example.studentrollcall.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.studentrollcall.R
import com.example.studentrollcall.databinding.FragmentClassBinding
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.viewmodel.EntryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ClassFragment: Fragment(R.layout.fragment_class) {
    private val TAG = "ClassFragment"
    private var _binding: FragmentClassBinding? = null
    private val binding get() = _binding!!

    private val args: ClassFragmentArgs by navArgs()
    private val entryViewModel: EntryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _class: Class = args.classToEdit
        entryViewModel.getEntries(_class).observe(viewLifecycleOwner) { entries ->
//            Log.d(TAG, entries.toString())
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                if (isActive) {
                    // Draw table
                    val width = _class.currentSession
                    val entryGroup = entries.groupBy { it.userId }
                    val height = entryGroup.count()

                    binding.tableLayout.outLineTable(width, height)
                    binding.tableLayout.fillData(entryGroup)

                } else {
                    Log.w(TAG, "Lifecycle ended early")
                }
            }
        }
    }

}

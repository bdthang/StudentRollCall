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
import com.example.studentrollcall.databinding.FragmentStatisticBinding
import com.example.studentrollcall.model.Entry
import com.example.studentrollcall.model.User
import com.example.studentrollcall.viewmodel.EntryViewModel
import com.example.studentrollcall.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.HashMap

class StatisticFragment : Fragment(R.layout.fragment_statistic) {
    private val TAG = "StatisticFragment"
    private var _binding: FragmentStatisticBinding? = null
    private val binding get() = _binding!!
    private val args: StatisticFragmentArgs by navArgs()
    private val entryViewModel: EntryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _class = args.classToView

        binding.apply {
            tvClassName.text = _class.title
            tvSessionCount.text = _class.currentSession.toString()

            userViewModel.getUsers(_class).observe(viewLifecycleOwner) { students ->
                if (students != null) {
                    entryViewModel.getEntries(_class).observe(viewLifecycleOwner) { entries ->

                        val entryMap = HashMap<String, MutableList<Entry>>()
                        entries.groupByTo(entryMap) { it.userId }

                        val result = HashMap<User, MutableList<Entry>>()
                        students.forEach { student ->
                            val privateEntryList = entryMap[student.uid]
                            if (privateEntryList != null) {
                                result[student] = privateEntryList
                            } else {
                                result[student] = mutableListOf()
                            }
                        }
//                    Log.d(TAG, result.toString())
                        val missOne: MutableList<String> = mutableListOf()
                        val missTwo: MutableList<String> = mutableListOf()
                        val missThreePlus: MutableList<String> = mutableListOf()
                        result.forEach { (user, entries) ->
                            val absence = _class.currentSession - entries.count()
                            when {
                                absence == 1 -> {
                                    missOne.add(user.name)
                                }
                                absence == 2 -> {
                                    missTwo.add(user.name)
                                }
                                absence >= 3 -> {
                                    missThreePlus.add(user.name)
                                }
                            }
                        }

                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            if (isActive) {
                                tvStudentCount.text = students.count().toString()
                                tvOne.text = missOne.joinToString("\n")
                                tvTwo.text = missTwo.joinToString("\n")
                                tvThree.text = missThreePlus.joinToString("\n")
                            } else {
                                Log.w(TAG, "Lifecycle ended early")
                            }
                        }

                    }

                }
            }
        }
    }
}
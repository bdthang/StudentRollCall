package com.example.studentrollcall.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentrollcall.R
import com.example.studentrollcall.customview.TableLayoutPlus
import com.example.studentrollcall.databinding.FragmentClassBinding
import com.example.studentrollcall.helper.reverseOrderOfWords
import com.example.studentrollcall.model.Class
import com.example.studentrollcall.model.Entry
import com.example.studentrollcall.model.User
import com.example.studentrollcall.viewmodel.ClassViewModel
import com.example.studentrollcall.viewmodel.EntryViewModel
import com.example.studentrollcall.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

class ClassFragment : Fragment(R.layout.fragment_class), TableLayoutPlus.OnItemClickListener {
    private val TAG = "ClassFragment"
    private var _binding: FragmentClassBinding? = null
    private val binding get() = _binding!!
    private lateinit var _class: Class

    private val args: ClassFragmentArgs by navArgs()
    private val entryViewModel: EntryViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val classViewModel: ClassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        _class = args.classToEdit

        binding.tableLayout.onItemClickListener = this

        userViewModel.getUserData().observe(viewLifecycleOwner) { user ->
            if (user.teacher) {
                binding.fabAction.setOnClickListener {
                    teacherAddSessionDialog(_class)
                }
            } else {
                val endTime: Long = _class.timeStart.time + _class.timeLimit * 60 * 1000
                val currentTime: Long = Calendar.getInstance().timeInMillis
                if (currentTime < endTime) {
                    binding.fabAction.setImageResource(R.drawable.ic_baseline_check_24)

                    binding.fabAction.setOnClickListener {
                        val action =
                            ClassFragmentDirections.actionClassFragmentToTallyFragment(_class)
                        findNavController().navigate(action)
                    }
                } else {
                    binding.fabAction.visibility = View.GONE
                }

            }
        }

        userViewModel.getUsers(_class).observe(viewLifecycleOwner) { students ->
            if (students != null) {

                entryViewModel.getEntries(_class).observe(viewLifecycleOwner) { entries ->

                    val entryMap = HashMap<String, MutableList<Entry>>()
                    entries.groupByTo(entryMap) { it.userId }
                    students.sortedBy { reverseOrderOfWords(it.name) }

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

                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        if (isActive) {
                            binding.tableLayout.drawHeader(_class.currentSession)
                            binding.tableLayout.drawTable(result)
                        } else {
                            Log.w(TAG, "Lifecycle ended early")
                        }
                    }

                }

            }
        }
    }

    private fun teacherAddSessionDialog(_class: Class) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.create_session_confirmation))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                classViewModel.createNewSession(_class).observe(viewLifecycleOwner) {
                    if (it == 0) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.session_created),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(entry: Entry) {
        val action = ClassFragmentDirections.actionClassFragmentToPhotoFragment(entry)
        findNavController().navigate(action)
    }

    override fun onUserClick(user: User) {
        val action = ClassFragmentDirections.actionClassFragmentToProfileReadFragment(user)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_class, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_statistic) {
            val action = ClassFragmentDirections.actionClassFragmentToStatisticFragment(_class)
            findNavController().navigate(action)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

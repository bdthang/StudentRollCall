package com.example.studentrollcall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.studentrollcall.adapter.ClassAdapter
import com.example.studentrollcall.databinding.ActivityMainBinding
import com.example.studentrollcall.viewmodel.ClassViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var classAdapter: ClassAdapter
    private lateinit var binding: ActivityMainBinding

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        classAdapter = ClassAdapter()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = classAdapter

        val classViewModel = ViewModelProvider(this).get(ClassViewModel::class.java)
        classViewModel.getClassData().observe(this) {
            classAdapter.classes = it
            classAdapter.notifyDataSetChanged()
            Log.d(TAG, "$it")
        }
    }
}
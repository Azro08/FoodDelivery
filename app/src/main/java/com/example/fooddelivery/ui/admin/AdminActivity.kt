package com.example.fooddelivery.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fooddelivery.R
import com.example.fooddelivery.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private var _binding : ActivityAdminBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
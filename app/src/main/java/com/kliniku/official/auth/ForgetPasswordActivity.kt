package com.kliniku.official.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kliniku.official.R
import com.kliniku.official.databinding.ActivityForgetPasswordBinding
import com.kliniku.official.databinding.CustomToolbarBinding

@Suppress("DEPRECATION")
class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var toolbarBinding: CustomToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        toolbarBinding = CustomToolbarBinding.bind(binding.toolbar.root)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        // Set as action bar
        setSupportActionBar(toolbarBinding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
        toolbarBinding.toolbarTitle.text = getString(R.string.forget_password_title)
        // Handle back button
        toolbarBinding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
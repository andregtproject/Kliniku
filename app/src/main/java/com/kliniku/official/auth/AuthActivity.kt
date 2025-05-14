package com.kliniku.official.auth

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.kliniku.official.R
import com.kliniku.official.auth.util.LoadingUtils
import com.kliniku.official.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var adapter: AuthPagerAdapter
    private var currentTabPosition = 0
    private var isLoginMode = false
    private lateinit var loadingOverlay: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingOverlay = findViewById(R.id.loadingLayout)

        // Extract mode from intent
        isLoginMode = intent.getBooleanExtra(KEY_LOGIN_MODE, false)

        savedInstanceState?.let {
            currentTabPosition = it.getInt(KEY_CURRENT_TAB, 0)
            isLoginMode = it.getBoolean(KEY_LOGIN_MODE, false)
        }

        setupUI()
        setupViewPager()
        setupTabLayout()
    }

    private fun setupUI() {
        with(binding) {
            if (isLoginMode) {
                welcomeTitle.setText(R.string.account_title)
                authDesc.setText(R.string.login_desc)
            } else {
                welcomeTitle.setText(R.string.account_title)
                authDesc.setText(R.string.register_desc)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_TAB, binding.viewPager.currentItem)
        outState.putBoolean(KEY_LOGIN_MODE, isLoginMode)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Force update tab layout when orientation changes
        binding.tabLayout.requestLayout()
    }

    private fun setupViewPager() {
        // Simpan posisi tab saat ini sebelum membuat adapter baru
        currentTabPosition = binding.viewPager.currentItem

        adapter = AuthPagerAdapter(this, isLoginMode)
        binding.viewPager.adapter = adapter

        // Set posisi tab tersimpan
        binding.viewPager.setCurrentItem(currentTabPosition, false)
    }

    // Switch tab
    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.email)
                1 -> tab.text = getString(R.string.phone_number_tab)
            }
        }.attach()

        val tabLayout = binding.tabLayout
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            tab?.view?.layoutParams?.width = 0
        }
    }

    // switch login/register
    fun switchToLogin() {
        // Simpan posisi tab saat ini sebelum berpindah mode
        currentTabPosition = binding.viewPager.currentItem
        isLoginMode = true
        setupUI()
        setupViewPager()
    }

    fun switchToRegister() {
        // Simpan posisi tab saat ini sebelum berpindah mode
        currentTabPosition = binding.viewPager.currentItem
        isLoginMode = false
        setupUI()
        setupViewPager()
    }

    fun showLoading() {
        LoadingUtils.showLoading(window, loadingOverlay)
    }

    fun hideLoading() {
        LoadingUtils.hideLoading(window, loadingOverlay)
    }

    companion object {
        const val KEY_CURRENT_TAB = "CURRENT_TAB"
        const val KEY_LOGIN_MODE = "LOGIN_MODE"
    }
}
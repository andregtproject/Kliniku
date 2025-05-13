package com.kliniku.official.auth.register

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.kliniku.official.R
import com.kliniku.official.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var adapter: RegisterPagerAdapter
    private var currentTabPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentTabPosition = it.getInt("CURRENT_TAB", 0)
        }

        setupViewPager()
        setupTabLayout()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("CURRENT_TAB", binding.registerViewPager.currentItem)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Force update tab layout when orientation changes
        binding.registerTabLayout.requestLayout()
    }

    private fun setupViewPager() {
        adapter = RegisterPagerAdapter(this)
        binding.registerViewPager.adapter = adapter

        // Set saved position after adapter is set
        binding.registerViewPager.setCurrentItem(currentTabPosition, false)

    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.registerTabLayout, binding.registerViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.email)
                1 -> tab.text = getString(R.string.phone_number_tab)
            }
        }.attach()

        val tabLayout = binding.registerTabLayout
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            tab?.view?.layoutParams?.width = 0
        }
    }
}
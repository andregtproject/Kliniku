package com.kliniku.official.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.kliniku.official.R
import com.kliniku.official.auth.AuthActivity
import com.kliniku.official.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    // Menggunakan ViewModel untuk menyimpan state
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup adapter dengan data dari ViewModel
        val adapter = OnboardingAdapter(viewModel.onboardingItems)
        binding.viewPager.adapter = adapter

        setupViewPager()
        setupButtons()
        observeViewModel()
    }

    private fun setupViewPager() {
        // Konfigurasi ViewPager dan Dots Indicator
        binding.viewPager.isUserInputEnabled = true
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.dotsIndicator.attachTo(binding.viewPager)

        // Kembalikan ke posisi yang tersimpan di ViewModel setelah rotasi
        viewModel.currentPosition.value?.let { position ->
            binding.viewPager.setCurrentItem(position, false)
            updateUI(position)
        }

        // Register callback untuk memperbarui UI dan ViewModel saat halaman berubah
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentPosition(position)
                updateUI(position)
            }
        })
    }

    private fun setupButtons() {
        binding.buttonSkip.setOnClickListener {
            navigateToAccountActivity()
        }

        binding.buttonNext.setOnClickListener {
            val currentPosition = viewModel.currentPosition.value ?: 0
            if (currentPosition < viewModel.onboardingItems.size - 1) {
                binding.viewPager.currentItem = currentPosition + 1
            } else {
                navigateToAccountActivity()
            }
        }
    }

    private fun observeViewModel() {
        // Observe perubahan posisi dari ViewModel
        viewModel.currentPosition.observe(this) { position ->
            updateUI(position)
        }
    }

    // visibility button
    private fun updateUI(position: Int) {
        val item = viewModel.onboardingItems[position]
        binding.textTitle.setText(item.title)

        if (item.description != 0) {
            binding.textDescription.setText(item.description)
            binding.textDescription.visibility = View.VISIBLE
        } else {
            binding.textDescription.visibility = View.GONE
        }

        if (position == viewModel.onboardingItems.size - 1) {
            binding.buttonSkip.visibility = View.GONE
            binding.buttonNext.setText(R.string.mulai_sekarang)
        } else {
            binding.buttonSkip.visibility = View.VISIBLE
            binding.buttonNext.setText(R.string.lanjut)
        }
    }

    private fun navigateToAccountActivity() {
        val sharedPreferences = getSharedPreferences("kliniku_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
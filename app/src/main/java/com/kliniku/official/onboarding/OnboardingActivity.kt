package com.kliniku.official.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.kliniku.official.MainActivity
import com.kliniku.official.R
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var buttonSkip: Button
    private lateinit var buttonNext: Button
    private lateinit var textTitle: TextView
    private lateinit var textDescription: TextView
    private lateinit var dotsIndicator: DotsIndicator

    // Menggunakan ViewModel untuk menyimpan state
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        initViews()

        // Setup adapter dengan data dari ViewModel
        val adapter = OnboardingAdapter(viewModel.onboardingItems)
        viewPager.adapter = adapter

        setupViewPager()
        setupButtons()
        observeViewModel()
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        buttonSkip = findViewById(R.id.buttonSkip)
        buttonNext = findViewById(R.id.buttonNext)
        textTitle = findViewById(R.id.textTitle)
        textDescription = findViewById(R.id.textDescription)
        dotsIndicator = findViewById(R.id.dotsIndicator)
    }

    private fun setupViewPager() {
        // Konfigurasi ViewPager dan Dots Indicator
        viewPager.isUserInputEnabled = true
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        dotsIndicator.attachTo(viewPager)

        // Kembalikan ke posisi yang tersimpan di ViewModel setelah rotasi
        viewModel.currentPosition.value?.let { position ->
            viewPager.setCurrentItem(position, false)
            updateUI(position)
        }

        // Register callback untuk memperbarui UI dan ViewModel saat halaman berubah
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentPosition(position)
                updateUI(position)
            }
        })
    }

    private fun setupButtons() {
        buttonSkip.setOnClickListener {
            navigateToMainActivity()
        }

        buttonNext.setOnClickListener {
            val currentPosition = viewModel.currentPosition.value ?: 0
            if (currentPosition < viewModel.onboardingItems.size - 1) {
                viewPager.currentItem = currentPosition + 1
            } else {
                navigateToMainActivity()
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
        textTitle.setText(item.title)

        if (item.description != 0) {
            textDescription.setText(item.description)
            textDescription.visibility = View.VISIBLE
        } else {
            textDescription.visibility = View.GONE
        }

        if (position == viewModel.onboardingItems.size - 1) {
            buttonSkip.visibility = View.GONE
            buttonNext.setText(R.string.mulai_sekarang)
        } else {
            buttonSkip.visibility = View.VISIBLE
            buttonNext.setText(R.string.lanjut)
        }
    }

    private fun navigateToMainActivity() {
        val sharedPreferences = getSharedPreferences("kliniku_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
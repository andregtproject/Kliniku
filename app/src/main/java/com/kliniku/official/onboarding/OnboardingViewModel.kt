package com.kliniku.official.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kliniku.official.R

class OnboardingViewModel : ViewModel() {
    // Menyimpan posisi current page(slide)
    private val _currentPosition = MutableLiveData<Int>(0)
    val currentPosition: LiveData<Int> = _currentPosition

    // memperbarui posisi halaman
    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    // Data onboarding items disimpan di ViewModel
    val onboardingItems = listOf(
        OnboardingItem(R.drawable.onboard1, R.string.onboard_title1, 0),
        OnboardingItem(R.drawable.onboard2, R.string.onboard_title2, 0),
        OnboardingItem(R.drawable.onboard3, R.string.onboard_title3, R.string.onboard_desc3)
    )
}
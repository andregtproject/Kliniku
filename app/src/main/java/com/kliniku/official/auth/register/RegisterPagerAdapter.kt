package com.kliniku.official.auth.register

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RegisterPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EmailRegisterFragment()
            1 -> PhoneRegisterFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
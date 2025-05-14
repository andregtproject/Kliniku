package com.kliniku.official.auth

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AuthPagerAdapter(
    private val activity: AppCompatActivity,
    private val isLoginMode: Boolean
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (isLoginMode) {
            when (position) {
                0 -> AuthBaseFragment.newInstance(AuthBaseFragment.AuthMode.LOGIN_EMAIL)
                1 -> AuthBaseFragment.newInstance(AuthBaseFragment.AuthMode.LOGIN_PHONE)
                else -> throw IllegalArgumentException("Invalid position")
            }
        } else {
            when (position) {
                0 -> AuthBaseFragment.newInstance(AuthBaseFragment.AuthMode.REGISTER_EMAIL)
                1 -> AuthBaseFragment.newInstance(AuthBaseFragment.AuthMode.REGISTER_PHONE)
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }
}
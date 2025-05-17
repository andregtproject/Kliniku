package com.kliniku.official.auth.profile_step

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileStepAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val fragments = mutableListOf<Fragment?>()

    init {
        fragments.addAll(List(itemCount) { null })
    }

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RoleFragment().also { fragments[0] = it }
            1 -> AddressFragment().also { fragments[1] = it }
            2 -> GenderFragment().also { fragments[2] = it }
            else -> PhotoFragment().also { fragments[3] = it }
        }
    }

    fun getFragment(position: Int): Fragment? {
        if (position in 0 until fragments.size) {
            return fragments[position] ?: createFragment(position).also {
                fragments[position] = it
            }
        }
        return null
    }
}
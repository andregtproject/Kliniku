package com.kliniku.official.auth.profile_step

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileStepAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    // Mapping fragment positions to their instances
    private val fragmentMap = mutableMapOf<Int, Fragment>()

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> RoleFragment()
            1 -> AddressFragment()
            2 -> GenderFragment()
            else -> PhotoFragment()
        }

        // Store the fragment instance in the map
        fragmentMap[position] = fragment
        return fragment
    }

    // getFragment to retrieve a fragment by its position
    fun getFragment(position: Int): Fragment? {
        return fragmentMap[position]
    }
}
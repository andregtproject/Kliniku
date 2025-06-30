package com.kliniku.official

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kliniku.official.pasien.home.HomeFragment
import com.kliniku.official.databinding.ActivityMainBinding
import com.kliniku.official.pasien.booking.BookingFragment
import com.kliniku.official.pasien.chat.ChatFragment
import com.kliniku.official.pasien.profile.ProfileFragment
import com.kliniku.official.pasien.history.HistoryFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragments = mapOf(
        R.id.nav_home to HomeFragment(),
        R.id.nav_booking to BookingFragment(),
        R.id.nav_chat to ChatFragment(),
        R.id.nav_history to HistoryFragment(),
        R.id.nav_profile to ProfileFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        setCurrentFragment(fragments[R.id.nav_home])
    }

    private fun initNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment = fragments[item.itemId]
            setCurrentFragment(selectedFragment)
            selectedFragment != null
        }
    }

    private fun setCurrentFragment(fragment: Fragment?) {
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, it)
                .commit()
        }
    }
}

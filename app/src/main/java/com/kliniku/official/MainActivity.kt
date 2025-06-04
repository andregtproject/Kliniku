package com.kliniku.official

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kliniku.official.pasien.home.HomeFragment
import com.kliniku.official.databinding.ActivityMainBinding
import com.kliniku.official.pasien.booking.PemesananFragment
import com.kliniku.official.pasien.chat.PesanFragment
import com.kliniku.official.pasien.profile.ProfilFragment
import com.kliniku.official.pasien.history.RiwayatFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val fragments = mapOf(
        R.id.nav_home to HomeFragment(),
        R.id.nav_booking to PemesananFragment(),
        R.id.nav_chat to PesanFragment(),
        R.id.nav_history to RiwayatFragment(),
        R.id.nav_profile to ProfilFragment()
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

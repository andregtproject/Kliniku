package com.kliniku.official

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kliniku.official.databinding.ActivityMainBinding
import com.kliniku.official.pasien.booking.PemesananFragment
import com.kliniku.official.pasien.chat.PesanFragment
import com.kliniku.official.pasien.profile.ProfilFragment
import com.kliniku.official.pasien.history.RiwayatFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kliniku.official.pasien.home.HomeFragment

class BottomNavbarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bottom_navbar)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val berandaFragment = HomeFragment()
        val pemesananFragment = PemesananFragment()
        val pesanFragment = PesanFragment()
        val riwayatFragment = RiwayatFragment()
        val profilFragment = ProfilFragment()
        setCurrentFragment(berandaFragment)


        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> setCurrentFragment(berandaFragment)
                R.id.nav_booking -> setCurrentFragment(pemesananFragment)
                R.id.nav_chat -> setCurrentFragment(pesanFragment)
                R.id.nav_history -> setCurrentFragment(riwayatFragment)
                R.id.nav_profile -> setCurrentFragment(profilFragment)
            }
            true
        }

    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}
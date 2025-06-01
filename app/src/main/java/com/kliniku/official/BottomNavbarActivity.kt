package com.kliniku.official

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.kliniku.official.beranda.BerandaFragment
import com.kliniku.official.databinding.ActivityMainBinding
import com.kliniku.official.pemesanan.PemesananFragment
import com.kliniku.official.pesan.PesanFragment
import com.kliniku.official.profil.ProfilFragment
import com.kliniku.official.riwayat.RiwayatFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavbarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bottom_navbar)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val berandaFragment = BerandaFragment()
        val pemesananFragment = PemesananFragment()
        val pesanFragment = PesanFragment()
        val riwayatFragment = RiwayatFragment()
        val profilFragment = ProfilFragment()
        setCurrentFragment(berandaFragment)


        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_beranda -> setCurrentFragment(berandaFragment)
                R.id.nav_pemesanan -> setCurrentFragment(pemesananFragment)
                R.id.nav_pesan -> setCurrentFragment(pesanFragment)
                R.id.nav_riwayat -> setCurrentFragment(riwayatFragment)
                R.id.nav_profil -> setCurrentFragment(profilFragment)
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
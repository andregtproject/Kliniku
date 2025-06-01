package com.kliniku.official

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.kliniku.official.auth.AuthActivity
import com.kliniku.official.databinding.ActivitySplashBinding
import com.kliniku.official.onboarding.OnboardingActivity
import pl.droidsonroids.gif.GifDrawable

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gifDrawable = binding.imageSplash.drawable as GifDrawable
        val gifDuration = gifDrawable.duration.toLong()

        // handler untuk onboarding hanya ditampilkan saat aplikasi pertama kali diinstall
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPrefs = getSharedPreferences("kliniku_prefs", MODE_PRIVATE)
            val onboardingCompleted = sharedPrefs.getBoolean("onboarding_completed", false)

            val intent = if (onboardingCompleted) {
                Intent(this, AuthActivity::class.java)
            } else {
                Intent(this, OnboardingActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, gifDuration + 200L)
    }
}
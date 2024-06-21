package com.dicoding.dapurnusantara.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.dapurnusantara.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Menjalankan handler untuk delay 2500 milidetik
        Handler(Looper.getMainLooper()).postDelayed({
            // Intent ke OnBoardingActivity
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()  // Menutup SplashActivity agar tidak kembali saat menekan tombol back
        }, 2500)  // 2500 milidetik delay
    }
}
package com.dicoding.dapurnusantara.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.dicoding.dapurnusantara.R
import com.dicoding.dapurnusantara.adapter.OnboardingPagerAdapter
import com.dicoding.dapurnusantara.ui.login.LoginActivity
import com.dicoding.dapurnusantara.ui.register.RegisterActivity

class OnboardingActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var registerButton: Button? = null
    private var loginButton: Button? = null
    private var pagerAdapter: OnboardingPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        registerButton = findViewById(R.id.btnRegisterOB)
        loginButton = findViewById(R.id.btnLoginOB)

        pagerAdapter = OnboardingPagerAdapter(this)
        viewPager?.adapter = pagerAdapter

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Not needed for this implementation
            }

            override fun onPageSelected(position: Int) {
                updateButtons(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not needed for this implementation
            }
        })

        registerButton?.setOnClickListener {
            // Start RegisterActivity when the register button is clicked
            val intent = Intent(this@OnboardingActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton?.setOnClickListener {
            // Start LoginActivity when the login button is clicked
            val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        updateButtons(0) // Initialize button states
    }

    private fun updateButtons(position: Int) {
        when (position) {
            0 -> {
                registerButton?.visibility = View.VISIBLE
                loginButton?.visibility = View.VISIBLE
            }
            pagerAdapter?.count?.minus(1) -> {
                registerButton?.visibility = View.VISIBLE
                loginButton?.visibility = View.VISIBLE
            }
            else -> {
                registerButton?.visibility = View.VISIBLE
                loginButton?.visibility = View.VISIBLE
            }
        }
    }
}

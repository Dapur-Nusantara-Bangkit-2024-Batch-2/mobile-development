package com.dicoding.dapurnusantara.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.dicoding.dapurnusantara.MainActivity
import com.dicoding.dapurnusantara.UserPreferences
import com.dicoding.dapurnusantara.adapter.OnboardingPagerAdapter
import com.dicoding.dapurnusantara.databinding.ActivityOnboardingBinding
import com.dicoding.dapurnusantara.ui.login.LoginActivity
import com.dicoding.dapurnusantara.ui.login.dataStore
import com.dicoding.dapurnusantara.ui.register.RegisterActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OnboardingActivity : AppCompatActivity() {

        private lateinit var binding: ActivityOnboardingBinding
        private lateinit var userPreferences: UserPreferences
        private var pagerAdapter: OnboardingPagerAdapter? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityOnboardingBinding.inflate(layoutInflater)
            setContentView(binding.root)

            userPreferences = UserPreferences.getInstance(dataStore)
            checkLoginSession()

            pagerAdapter = OnboardingPagerAdapter(this)
            binding.viewPager.adapter = pagerAdapter

            binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    updateButtons(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })

            binding.btnRegisterOB.setOnClickListener {
                val intent = Intent(this@OnboardingActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            binding.btnLoginOB.setOnClickListener {
                val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            updateButtons(0)
        }

        private fun updateButtons(position: Int) {
            when (position) {
                0 -> {
                    binding.btnRegisterOB.visibility = View.VISIBLE
                    binding.btnLoginOB.visibility = View.VISIBLE
                }

                pagerAdapter?.count?.minus(1) -> {
                    binding.btnRegisterOB.visibility = View.VISIBLE
                    binding.btnLoginOB.visibility = View.VISIBLE
                }

                else -> {
                    binding.btnRegisterOB.visibility = View.VISIBLE
                    binding.btnLoginOB.visibility = View.VISIBLE
                }
            }
        }

        private fun checkLoginSession() {
            userPreferences.getLoginSession().onEach { isLoggedIn ->
                if (isLoggedIn) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.launchIn(lifecycleScope)
        }
    }
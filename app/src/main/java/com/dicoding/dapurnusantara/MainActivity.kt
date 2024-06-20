package com.dicoding.dapurnusantara

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.dicoding.dapurnusantara.databinding.ActivityMainBinding
import com.dicoding.dapurnusantara.ui.home.HomeFragment
import com.dicoding.dapurnusantara.ui.home.ProfileFragment
import com.dicoding.dapurnusantara.ui.home.ScanFragment

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    private val isLoggedIn = false // Flag untuk status login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        replaceFragment(HomeFragment())

        binding!!.bnMain.setOnItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.home) {
                replaceFragment(HomeFragment())
            } else if (item.itemId == R.id.scanfood) {
                replaceFragment(ScanFragment())
                //            } else if (item.getItemId() == R.id.favorite) {
//                replaceFragment(new FavoriteFragment());
            } else if (item.itemId == R.id.profile) {
                replaceFragment(ProfileFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flMain, fragment)
        fragmentTransaction.commit()
    }
}
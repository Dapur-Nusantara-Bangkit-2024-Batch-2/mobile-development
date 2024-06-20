package com.dicoding.dapurnusantara.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dapurnusantara.R
import com.dicoding.dapurnusantara.UserPreferences
import com.dicoding.dapurnusantara.ui.ViewModelFactory
import com.dicoding.dapurnusantara.ui.login.LoginActivity
import com.dicoding.dapurnusantara.ui.login.UserLoginViewModel
import com.dicoding.dapurnusantara.ui.login.dataStore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var viewModel: UserLoginViewModel
    private lateinit var tvUsernameProfile: TextView
    private lateinit var tvEmailProfile: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(requireContext().dataStore)))
            .get(UserLoginViewModel::class.java)

        // Initialize TextViews
        tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile)
        tvEmailProfile = view.findViewById(R.id.tvEmailProfile)

        // Observe name changes
        viewModel.getName().observe(viewLifecycleOwner) { username ->
            tvUsernameProfile.text = username
        }

        // Observe email changes
        viewModel.getEmail().observe(viewLifecycleOwner) { email ->
            tvEmailProfile.text = email
        }

        // Handle logout button click
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Clear login data
        viewModel.clearDataLogin()

        // Navigate back to LoginActivity
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()
    }
}


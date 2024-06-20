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
    // Inject ViewModel and other necessary components
    private lateinit var viewModel: UserLoginViewModel

    private lateinit var tvUsernameProfile: TextView
    private lateinit var tvEmailProfile: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory(UserPreferences.getInstance(requireContext().dataStore))).get(UserLoginViewModel::class.java)

        // Initialize views
        tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile)
        tvEmailProfile = view.findViewById(R.id.tvEmailProfile)

        // Observe changes in username and email
        viewModel.getName().observe(viewLifecycleOwner) { username ->
            tvUsernameProfile.text = username
        }
        viewModel.getEmail().observe(viewLifecycleOwner) { email ->
            tvEmailProfile.text = email
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logout()
        }
    }

    // Handle logout action
    private fun logout() {
        viewModel.clearDataLogin() // Clear session data
        startActivity(Intent(activity, LoginActivity::class.java)) // Navigate to LoginActivity
        activity?.finish() // Close the current activity (ProfileFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

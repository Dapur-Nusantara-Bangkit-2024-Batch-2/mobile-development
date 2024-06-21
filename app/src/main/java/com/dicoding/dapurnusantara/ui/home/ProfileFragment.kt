package com.dicoding.dapurnusantara.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import com.dicoding.dapurnusantara.UserPreferences
import com.dicoding.dapurnusantara.databinding.FragmentProfileBinding
import com.dicoding.dapurnusantara.ui.ViewModelFactory
import com.dicoding.dapurnusantara.ui.login.LoginActivity
import com.dicoding.dapurnusantara.ui.login.UserLoginViewModel
import com.dicoding.dapurnusantara.ui.login.dataStore

class ProfileFragment : Fragment() {
    private lateinit var viewModel: UserLoginViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(requireContext().dataStore))
        )[UserLoginViewModel::class.java]

        viewModel.getName().observe(viewLifecycleOwner) { username ->
            binding.tvUsernameProfile.text = username
        }

        viewModel.getEmail().observe(viewLifecycleOwner) { email ->
            binding.tvEmailProfile.text = email
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        viewModel.clearDataLogin()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



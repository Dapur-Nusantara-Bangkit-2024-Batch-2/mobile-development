package com.dicoding.dapurnusantara.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dapurnusantara.UserPreferences
import com.dicoding.dapurnusantara.ui.login.UserLoginViewModel

class ViewModelFactory(private val pref: UserPreferences) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserLoginViewModel::class.java)) {
            return UserLoginViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
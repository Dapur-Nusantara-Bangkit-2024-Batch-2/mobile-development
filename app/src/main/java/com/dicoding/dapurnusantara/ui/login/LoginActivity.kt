package com.dicoding.dapurnusantara.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dapurnusantara.MainActivity
import com.dicoding.dapurnusantara.R
import com.dicoding.dapurnusantara.UserPreferences
import com.dicoding.dapurnusantara.databinding.ActivityLoginBinding
import com.dicoding.dapurnusantara.dataclass.LoginDataAccount
import com.dicoding.dapurnusantara.ui.ViewModelFactory
import com.dicoding.dapurnusantara.ui.register.RegisterActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        observeViewModels()
    }

    private fun setupView() {
        binding.btnLogin.setOnClickListener {
            binding.CVEmail.clearFocus()
            binding.PasswordLogin.clearFocus()
            hideKeyboard()  // Menyembunyikan keyboard jika diperlukan

            if (isDataValid()) {
                val loginData = LoginDataAccount(
                    binding.CVEmail.text.toString().trim(),
                    binding.PasswordLogin.text.toString().trim()
                )
                loginViewModel.getResponseLogin(loginData)
            } else {
                handleValidationError()
            }
        }

        binding.CVEmail.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard(v)
            }
        }

        binding.PasswordLogin.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard(v)
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Fungsi untuk menampilkan keyboard
    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    // Fungsi untuk menyembunyikan keyboard
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }


    private fun observeViewModels() {
        val preferences = UserPreferences.getInstance(dataStore)
        val userLoginViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserLoginViewModel::class.java]

        userLoginViewModel.getLoginSession().observe(this) { sessionActive ->
            if (sessionActive) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        loginViewModel.messageLogin.observe(this) { message ->
            handleLoginResponse(loginViewModel.isErrorLogin, message, userLoginViewModel)
        }

        loginViewModel.isLoadingLogin.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun handleLoginResponse(
        isError: Boolean,
        message: String,
        userLoginViewModel: UserLoginViewModel
    ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (!isError) {
            loginViewModel.userLogin.value?.loginResult?.let { loginResult ->
                userLoginViewModel.saveLoginSession(true)
                userLoginViewModel.saveToken(loginResult.token)
                userLoginViewModel.saveName(loginResult.name)
            }
        }
    }

    private fun handleValidationError() {
        if (!binding.CVEmail.isEmailValid) {
            binding.CVEmail.error = getString(R.string.error_empty_email)
        }
        if (!binding.PasswordLogin.isPasswordValid) {
            binding.PasswordLogin.error = getString(R.string.error_empty_password)
        }
        Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show()
    }

    private fun isDataValid(): Boolean {
        return binding.CVEmail.isEmailValid && binding.PasswordLogin.isPasswordValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
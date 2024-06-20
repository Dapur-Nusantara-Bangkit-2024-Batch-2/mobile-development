package com.dicoding.dapurnusantara.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dapurnusantara.MainActivity
import com.dicoding.dapurnusantara.R
import com.dicoding.dapurnusantara.UserPreferences
import com.dicoding.dapurnusantara.databinding.ActivityRegisterBinding
import com.dicoding.dapurnusantara.dataclass.LoginDataAccount
import com.dicoding.dapurnusantara.dataclass.RegisterDataAccount
import com.dicoding.dapurnusantara.ui.ViewModelFactory
import com.dicoding.dapurnusantara.ui.login.LoginActivity
import com.dicoding.dapurnusantara.ui.login.LoginViewModel
import com.dicoding.dapurnusantara.ui.login.UserLoginViewModel
import com.dicoding.dapurnusantara.ui.login.dataStore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val mainViewModel: LoginViewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private val userLoginViewModel: UserLoginViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[UserLoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        observeLoginSession()
        observeRegistrationResponse()
        observeLoginResponse()
        observeLoadingState()
        setupClickListeners()
    }

    private fun setupActionBar() {
        supportActionBar?.title = resources.getString(R.string.createAccount)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun observeLoginSession() {
        userLoginViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                startActivity(
                    Intent(
                        this@RegisterActivity,
                        MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
            }
        }
    }

    private fun observeRegistrationResponse() {
        mainViewModel.messageRegist.observe(this) { messageRegist ->
            responseRegister(mainViewModel.isErrorRegist, messageRegist)
        }
    }

    private fun observeLoginResponse() {
        mainViewModel.messageLogin.observe(this) { messageLogin ->
            responseLogin(mainViewModel.isErrorLogin, messageLogin, userLoginViewModel)
        }
    }

    private fun observeLoadingState() {
        mainViewModel.isLoadingRegist.observe(this) { showLoading(it) }
        mainViewModel.isLoadingLogin.observe(this) { showLoading(it) }
    }

    private fun setupClickListeners() {
        binding.btnRegistAccount.setOnClickListener {
            validateAndRegisterUser()
        }

        binding.btnLoginRegist.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun validateAndRegisterUser() {
        binding.apply {
            RegistName.clearFocus()
            RegistEmail.clearFocus()
            RegistPassword.clearFocus()
        }

        if (binding.RegistName.isNameValid && binding.RegistEmail.isEmailValid && binding.RegistPassword.isPasswordValid) {
            val dataRegisterAccount = RegisterDataAccount(
                name = binding.RegistName.text.toString().trim(),
                email = binding.RegistEmail.text.toString().trim(),
                password = binding.RegistPassword.text.toString().trim()
            )

            mainViewModel.getResponseRegister(dataRegisterAccount)
        } else {
            validateFields()
            Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateFields() {
        if (!binding.RegistName.isNameValid) binding.RegistName.error =
            resources.getString(R.string.error_empty_name)
        if (!binding.RegistEmail.isEmailValid) binding.RegistEmail.error =
            resources.getString(R.string.error_empty_email)
        if (!binding.RegistPassword.isPasswordValid) binding.RegistPassword.error =
            resources.getString(R.string.error_empty_password)
    }

    private fun responseLogin(
        isError: Boolean,
        message: String,
        userLoginViewModel: UserLoginViewModel
    ) {
        if (!isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val user = mainViewModel.userLogin.value
            userLoginViewModel.saveLoginSession(true)
            userLoginViewModel.saveToken(user?.loginResult!!.token)
            userLoginViewModel.saveName(user.loginResult.name)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun responseRegister(isError: Boolean, message: String) {
        if (!isError) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            // Save username and email to UserPreferences
            val username = binding.RegistName.text.toString().trim()
            val email = binding.RegistEmail.text.toString().trim()

            userLoginViewModel.saveName(username)
            userLoginViewModel.saveEmail(email)

            // Proceed with login after successful registration
            val userLogin = LoginDataAccount(email, binding.RegistPassword.text.toString())
            mainViewModel.getResponseLogin(userLogin)
        } else {
            // Handle registration error
            if (message == "1") {
                binding.RegistEmail.setErrorMessage(
                    resources.getString(R.string.emailTaken),
                    binding.RegistEmail.text.toString()
                )
                Toast.makeText(this, resources.getString(R.string.emailTaken), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }
}

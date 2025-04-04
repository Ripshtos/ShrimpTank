package com.rip.shrimptank.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rip.shrimptank.R
import com.rip.shrimptank.ui.register.RegisterActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rip.shrimptank.model.user.UserModel
import com.rip.shrimptank.ui.MainActivity

class LoginActivity : AppCompatActivity() {

    private var auth = Firebase.auth
    private lateinit var emailAddressInputLayout: TextInputLayout
    private lateinit var emailAddressEditText: TextInputEditText
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var loginProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (auth.currentUser != null) {
            loggedInHandler()
        }

        initializeViews()
        toRegisterActivity()
        setupLoginButton()
    }

    private fun initializeViews() {
        emailAddressInputLayout = findViewById(R.id.layoutEmailAddress)
        emailAddressEditText = findViewById(R.id.editTextEmailAddress)
        passwordInputLayout = findViewById(R.id.layoutPassword)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.LogInButton)
        loginProgressBar = findViewById(R.id.loginProgressBar)
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            val email = emailAddressEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val syntaxChecksResult = validateUserCredentials(email, password)

            if (syntaxChecksResult) {
                // Show loading state
                showLoading(true)

                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        UserModel.instance.updateCurrentUser {
                            showLoading(false)
                            loggedInHandler()
                        }
                    }
                    .addOnFailureListener {
                        showLoading(false)
                        Toast.makeText(
                            this@LoginActivity,
                            "Your Email or Password is incorrect!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loginButton.text = ""  // Clear button text when loading
            loginProgressBar.visibility = View.VISIBLE
            loginButton.isEnabled = false
        } else {
            loginButton.text = "Log In"
            loginProgressBar.visibility = View.GONE
            loginButton.isEnabled = true
        }
    }

    private fun toRegisterActivity() {
        findViewById<TextView>(R.id.CreateAccountLinkTextView).setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loggedInHandler() {
        Toast.makeText(
            this@LoginActivity,
            "Login Successful",
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateUserCredentials(
        email: String,
        password: String
    ): Boolean {
        // Basic checks
        if (email.isEmpty()) {
            emailAddressInputLayout.error = "Email cannot be empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddressInputLayout.error = "Invalid email format"
            return false
        } else {
            emailAddressInputLayout.error = null
        }
        if (password.isEmpty()) {
            passwordInputLayout.error = "Password cannot be empty"
            return false
        } else {
            passwordInputLayout.error = null
        }
        return true
    }
}
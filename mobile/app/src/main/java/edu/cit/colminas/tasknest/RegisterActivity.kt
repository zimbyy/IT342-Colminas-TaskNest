package edu.cit.colminas.tasknest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import edu.cit.colminas.tasknest.model.RegisterRequest
import edu.cit.colminas.tasknest.model.RegisterResponse
import edu.cit.colminas.tasknest.network.ApiClient
import edu.cit.colminas.tasknest.network.AuthApiService
import kotlinx.coroutines.launch
import retrofit2.Response
import android.util.Patterns

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var ivBack: ImageView
    private lateinit var tilFirstName: TextInputLayout
    private lateinit var tilLastName: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilUsername: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvLogin: TextView
    
    private lateinit var authApiService: AuthApiService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        // Initialize API service
        authApiService = ApiClient.instance.create(AuthApiService::class.java)
        
        initViews()
        setupClickListeners()
    }
    
    private fun initViews() {
        ivBack = findViewById(R.id.ivBack)
        tilFirstName = findViewById(R.id.tilFirstName)
        tilLastName = findViewById(R.id.tilLastName)
        tilEmail = findViewById(R.id.tilEmail)
        tilUsername = findViewById(R.id.tilUsername)
        tilPassword = findViewById(R.id.tilPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar)
        tvLogin = findViewById(R.id.tvLogin)
    }
    
    private fun setupClickListeners() {
        ivBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        
        btnRegister.setOnClickListener {
            performRegister()
        }
        
        tvLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    
    private fun performRegister() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()
        
        // Clear previous errors
        tilFirstName.error = null
        tilLastName.error = null
        tilEmail.error = null
        tilUsername.error = null
        tilPassword.error = null
        tilConfirmPassword.error = null
        
        var isValid = true
        
        // Validate all fields are not empty
        if (firstName.isEmpty()) {
            tilFirstName.error = "First name is required"
            isValid = false
        }
        
        if (lastName.isEmpty()) {
            tilLastName.error = "Last name is required"
            isValid = false
        }
        
        if (email.isEmpty()) {
            tilEmail.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Please enter a valid email"
            isValid = false
        }
        
        if (username.isEmpty()) {
            tilUsername.error = "Username is required"
            isValid = false
        }
        
        if (password.isEmpty()) {
            tilPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        }
        
        if (confirmPassword.isEmpty()) {
            tilConfirmPassword.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        }
        
        if (!isValid) {
            return
        }
        
        // Show loading state
        showLoading(true)
        
        // Make API call
        lifecycleScope.launch {
            try {
                val registerRequest = RegisterRequest(username, password, email, firstName, lastName)
                val response: Response<RegisterResponse> = authApiService.register(registerRequest)
                
                showLoading(false)
                
                if (response.isSuccessful && response.body() != null) {
                    val registerResponse = response.body()!!
                    
                    if (registerResponse.success) {
                        Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                        
                        // Navigate back to Login
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    } else {
                        // Registration failed
                        val errorMessage = when {
                            registerResponse.error != null -> registerResponse.error.toString()
                            else -> "Registration failed. Please try again."
                        }
                        Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                } else {
                    // API call failed
                    val errorMessage = when (response.code()) {
                        409 -> "Username or email already exists"
                        400 -> "Invalid input data"
                        500 -> "Server error. Please try again later."
                        else -> "Registration failed: ${response.message()}"
                    }
                    Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(this@RegisterActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        btnRegister.isEnabled = !isLoading
        btnRegister.text = if (isLoading) "" else "Register"
    }
}

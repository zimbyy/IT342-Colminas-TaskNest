package edu.cit.colminas.tasknest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import edu.cit.colminas.tasknest.model.LoginRequest
import edu.cit.colminas.tasknest.model.LoginResponse
import edu.cit.colminas.tasknest.network.ApiClient
import edu.cit.colminas.tasknest.network.AuthApiService
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tilUsername: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvRegister: TextView
    
    private lateinit var authApiService: AuthApiService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Initialize API service
        authApiService = ApiClient.instance.create(AuthApiService::class.java)
        
        // Check if user is already logged in
        checkLoginStatus()
        
        initViews()
        setupClickListeners()
    }
    
    private fun checkLoginStatus() {
        val prefs = getSharedPreferences("tasknest_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        
        if (!token.isNullOrEmpty()) {
            // User is already logged in, navigate to Dashboard
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }
    
    private fun initViews() {
        tilUsername = findViewById(R.id.tilUsername)
        tilPassword = findViewById(R.id.tilPassword)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        tvRegister = findViewById(R.id.tvRegister)
    }
    
    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            performLogin()
        }
        
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    
    private fun performLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        
        // Validate input
        if (username.isEmpty()) {
            tilUsername.error = "Username is required"
            return
        }
        
        if (password.isEmpty()) {
            tilPassword.error = "Password is required"
            return
        }
        
        // Clear errors
        tilUsername.error = null
        tilPassword.error = null
        
        // Show loading state
        showLoading(true)
        
        // Make API call
        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response: Response<LoginResponse> = authApiService.login(loginRequest)
                
                showLoading(false)
                
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    
                    if (loginResponse.success && loginResponse.data != null) {
                        // Save user data to SharedPreferences
                        val prefs = getSharedPreferences("tasknest_prefs", Context.MODE_PRIVATE)
                        val editor = prefs.edit()
                        editor.putLong("userId", loginResponse.data.userId)
                        editor.putString("token", loginResponse.data.token)
                        editor.putString("username", loginResponse.data.username)
                        editor.putString("firstName", loginResponse.data.firstName)
                        editor.apply()
                        
                        Toast.makeText(this@MainActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                        
                        // Navigate to Dashboard
                        startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                        finish()
                    } else {
                        // Login failed
                        val errorMessage = when {
                            loginResponse.error != null -> loginResponse.error.toString()
                            else -> "Login failed. Please try again."
                        }
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                    }
                } else {
                    // API call failed
                    val errorMessage = when (response.code()) {
                        401 -> "Invalid username or password"
                        404 -> "User not found"
                        500 -> "Server error. Please try again later."
                        else -> "Login failed: ${response.message()}"
                    }
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(this@MainActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        btnLogin.isEnabled = !isLoading
        btnLogin.text = if (isLoading) "" else "Login"
    }
}
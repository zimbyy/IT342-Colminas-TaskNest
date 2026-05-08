package edu.cit.colminas.tasknest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    
    private lateinit var tvWelcome: TextView
    private lateinit var btnLogout: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        
        initViews()
        setupClickListeners()
        displayWelcomeMessage()
    }
    
    private fun initViews() {
        tvWelcome = findViewById(R.id.tvWelcome)
        btnLogout = findViewById(R.id.btnLogout)
    }
    
    private fun setupClickListeners() {
        btnLogout.setOnClickListener {
            logout()
        }
    }
    
    private fun displayWelcomeMessage() {
        val prefs = getSharedPreferences("tasknest_prefs", Context.MODE_PRIVATE)
        val firstName = prefs.getString("firstName", null)
        val username = prefs.getString("username", null)
        
        val welcomeText = when {
            !firstName.isNullOrEmpty() -> "Welcome, $firstName!"
            !username.isNullOrEmpty() -> "Welcome, $username!"
            else -> "Welcome to TaskNest!"
        }
        
        tvWelcome.text = welcomeText
    }
    
    private fun logout() {
        // Clear SharedPreferences
        val prefs = getSharedPreferences("tasknest_prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        
        // Navigate back to LoginActivity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

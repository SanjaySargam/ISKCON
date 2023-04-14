package com.example.iskcon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class feedbacksplash2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedbacksplash2)

        // Show the splash screen
        Handler().postDelayed({
            val intent = Intent(this, Feedback::class.java)
            startActivity(intent)
            finish()
        },3000)

}}
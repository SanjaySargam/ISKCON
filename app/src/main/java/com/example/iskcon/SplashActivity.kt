package com.example.iskcon

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val app_name = findViewById<TextView>(R.id.app_name)
        val typeface = ResourcesCompat.getFont(this, R.font.blacklist)
        app_name.setTypeface(typeface)


        val anim = AnimationUtils.loadAnimation(this, R.anim.myanim)
        app_name.animation = anim

        mAuth = FirebaseAuth.getInstance()
        FirebaseQuery.firestore = FirebaseFirestore.getInstance()

        Handler().postDelayed({
            if (mAuth!!.currentUser != null) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)

    }
}
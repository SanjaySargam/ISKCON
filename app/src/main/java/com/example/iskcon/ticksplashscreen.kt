package com.example.iskcon

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class ticksplashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticksplashscreen)

        // Create and start the MediaPlayer object
        val mediaPlayer = MediaPlayer.create(this, R.raw.ticksound)
        mediaPlayer.start()

        Handler().postDelayed({
            // Stop the sound effect
            mediaPlayer.stop()
            mediaPlayer.release()
            finish()
        },3000)
    }
}

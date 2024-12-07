package com.babumusai.sparkleai.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.babumusai.sparkleai.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var imgSplash: ImageView
    lateinit var textViewSplash: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgSplash = findViewById(R.id.imgSplash)
        textViewSplash = findViewById(R.id.textViewSplash)

        // load animation
        val anim_splash_entry = AnimationUtils.loadAnimation(this, R.anim.anim_splash_entry)
        val anim_splash_exit = AnimationUtils.loadAnimation(this, R.anim.anim_splash_exit)
        val anim_splash_text = AnimationUtils.loadAnimation(this, R.anim.anim_splash_text)
        imgSplash.startAnimation(anim_splash_entry)

        lifecycleScope.launch {
            delay(2000)
            imgSplash.startAnimation(anim_splash_exit)
            textViewSplash.startAnimation(anim_splash_text)
            delay(1000)
            val intent = Intent(this@SplashActivity, BehaviourActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
package com.givekesh.raters.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.givekesh.raters.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SplashActivity : AppCompatActivity() {
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            )
        )
        finish()
    }
}

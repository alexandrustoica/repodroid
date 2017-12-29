package com.ubb.alexandrustoica.reporter.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ubb.alexandrustoica.reporter.R

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    fun onLoginButtonClick(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun onRegisterButtonClick(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}

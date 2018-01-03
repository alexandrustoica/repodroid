package com.ubb.alexandrustoica.reporter.activity

import android.app.ActivityOptions.makeSceneTransitionAnimation
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ubb.alexandrustoica.reporter.R
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    fun onLoginButtonClick(view: View) {
        val options = makeSceneTransitionAnimation(this,
                android.util.Pair(loginButton, "loginButtonTransition"))
        startActivity(Intent(this, LoginActivity::class.java),
                options.toBundle())
    }

    fun onRegisterButtonClick(view: View) {
        val options = makeSceneTransitionAnimation(this,
                android.util.Pair(registerButton, "registerButtonTransition"))
        startActivity(Intent(this, RegisterActivity::class.java),
                options.toBundle())
    }
}

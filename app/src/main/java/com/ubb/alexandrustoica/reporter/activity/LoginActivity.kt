package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ubb.alexandrustoica.reporter.task.LoginRequestTask
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.domain.LoginRequestBody
import com.ubb.alexandrustoica.reporter.task.CompletedTask
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), CompletedTask<String> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onTaskCompleted(result: String) {
        println(result)
        if (result == "") BasicAlertDialog(
                this@LoginActivity,
                "Wrong Username or Password").show()
        else {
            saveTokenToSharedPreferences(result)
                .also { startActivity(Intent(this, ReportsActivity::class.java)) }
        }
    }

    fun onLoginButtonClick(view: View) {
        LoginRequestTask(this@LoginActivity).execute(
                LoginRequestBody(
                        usernameEditTextLogin.text.toString(),
                        passwordEditTextLogin.text.toString()))
    }

    private fun saveTokenToSharedPreferences(token: String) {
        getSharedPreferences(
                getString(R.string.preferences_file_key),
                Context.MODE_PRIVATE)
                .edit()
                .let { it.putString(getString(R.string.token), token) }
                .let { it.commit() }
    }
}

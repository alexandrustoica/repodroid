package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.domain.AsyncResponse
import com.ubb.alexandrustoica.reporter.domain.RegisterRequestBody
import com.ubb.alexandrustoica.reporter.task.CallbackAsyncResponse
import com.ubb.alexandrustoica.reporter.task.RegisterRequestTask
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), CallbackAsyncResponse<String, String> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun onTaskCompleted(result: AsyncResponse<String, String>) {
        result.ifResult {
            saveTokenToSharedPreferences(it)
                    .also { startActivity(Intent(this, ReportsActivity::class.java)) }
        }
        result.ifError { BasicAlertDialog(this@RegisterActivity, it).show() }
    }

    fun onRegisterButtonClick(view: View) {
        val password = passwordEditTextRegister.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        if (password == confirmPassword) {
            RegisterRequestTask(this@RegisterActivity).execute(
                    RegisterRequestBody(
                            usernameEditTextRegister.text.toString(),
                            nameEditText.text.toString(),
                            emailEditText.text.toString(),
                            password))
        } else BasicAlertDialog(this@RegisterActivity, "Passwords Don't Match").show()
    }

    private fun saveTokenToSharedPreferences(token: String) {
        getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE).edit()
                .let { it.putString(getString(R.string.token), token) }
                .let { it.commit() }
    }
}

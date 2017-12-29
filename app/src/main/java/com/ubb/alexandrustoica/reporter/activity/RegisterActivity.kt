package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.domain.RegisterRequestBody
import com.ubb.alexandrustoica.reporter.task.RegisterRequestTask
import com.ubb.alexandrustoica.reporter.task.CompletedTask
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), CompletedTask<String> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun onTaskCompleted(result: String) =
            if (result == "") showRegisterAlertToUser()
            else saveTokenToSharedPreferences(result)

    fun onRegisterButtonClick(view: View) {
        val password = passwordEditTextRegister.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString();
        if (password == confirmPassword) {
            RegisterRequestTask(this@RegisterActivity).execute(
                    RegisterRequestBody(
                            usernameEditTextRegister.text.toString(),
                            nameLableEditText.text.toString(),
                            emailEditText.text.toString(),
                            password))
        }
    }

    private fun showRegisterAlertToUser() {
        AlertDialog.Builder(this@RegisterActivity)
                .also { it.setMessage("Unavailable username or password too short") }
                .also { it.setTitle("Unable To Register!") }.show()
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

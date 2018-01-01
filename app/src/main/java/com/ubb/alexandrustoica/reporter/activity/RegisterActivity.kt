package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.rest.*
import kotlinx.android.synthetic.main.activity_register.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder

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
            val urlBuilder = UriComponentsBuilder
                    .fromHttpUrl("${getString(R.string.service_url_base)}/users/register")
            RestAsyncTask<RegisterRequestBody, String>(
                    this@RegisterActivity, HttpMethod.POST, urlBuilder,
                    NoHeaderStrategy(), object : ParameterizedTypeReference<String>() {},
                    { it.headers["Authorization"]?.get(0) ?: "" })
                    .execute(RegisterRequestBody(
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

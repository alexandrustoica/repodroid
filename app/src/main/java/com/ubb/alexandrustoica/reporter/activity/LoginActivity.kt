package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.rest.LoginRequestBody
import com.ubb.alexandrustoica.reporter.rest.CallbackTask
import com.ubb.alexandrustoica.reporter.rest.NoHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_login.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder


class LoginActivity : AppCompatActivity(), CallbackTask<AsyncResponse<String, String>> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onTaskCompleted(result: AsyncResponse<String, String>) {
        println(result)
        result.ifResult {
            saveTokenToSharedPreferences(it)
                    .also { startActivity(Intent(this, ReportsActivity::class.java)) }
        }
        result.ifError { BasicAlertDialog(this@LoginActivity, it).show() }
    }

    fun onLoginButtonClick(view: View) {
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/login")
        RestAsyncTask<LoginRequestBody, String>(
                this@LoginActivity, HttpMethod.POST, urlBuilder,
                NoHeaderStrategy(), object : ParameterizedTypeReference<String>() {},
                { it.headers["Authorization"]?.get(0) ?: "" })
                .execute(LoginRequestBody(
                        usernameEditTextLogin.text.toString(),
                        passwordEditTextLogin.text.toString()))
//        LoginRequestTask(this@LoginActivity).execute(
//                LoginRequestBody(
//                        usernameEditTextLogin.text.toString(),
//                        passwordEditTextLogin.text.toString()))
    }

    private fun saveTokenToSharedPreferences(token: String) {
        getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE).edit()
                .let { it.putString(getString(R.string.token), token) }
                .let { it.commit() }
    }
}

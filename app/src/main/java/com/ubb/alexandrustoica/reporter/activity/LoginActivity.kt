package com.ubb.alexandrustoica.reporter.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.domain.LoginRequestBody
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.rest.NoHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.OnAsyncResponseReadyCallback
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_login.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder

class LoginActivity : AppCompatActivity(),
        OnAsyncResponseReadyCallback<String, String> {

    private val getAuthorizationTokenFromServerResponse:
            (ResponseEntity<String>) -> String = {
        it.headers["Authorization"]?.get(0) ?: ""
    }

    private val whenAuthorizationTokenReady: (String) -> Unit = {
        saveTokenToSharedPreferences(it).also {
            startActivity(Intent(this, ReportsActivity::class.java))
        }
    }

    private val whenExceptionFromServer: (String) -> Unit = {
        BasicAlertDialog(this@LoginActivity, it).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usernameEditTextFromLoginActivity.pivotY = 200f
        passwordEditTextFromLoginActivity.pivotY = 200f
        usernameEditTextFromLoginActivity.scaleY = 0f
        AnimatorSet().also {
            it.playSequentially(
                    ObjectAnimator.ofFloat(passwordEditTextFromLoginActivity, "scaleY", 0f, 1f),
                    ObjectAnimator.ofFloat(usernameEditTextFromLoginActivity, "scaleY", 0f, 1f)
            )
        }.setDuration(400).start()
    }

    override fun onTaskCompleted(result: AsyncResponse<String, String>) {
        result.ifResult(whenAuthorizationTokenReady)
                .ifError(whenExceptionFromServer)
    }

    fun onLoginButtonClick(view: View) {
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/login")
        RestAsyncTask<LoginRequestBody, String>(
                context = this@LoginActivity,
                method = HttpMethod.POST,
                urlBuilder = urlBuilder,
                headerStrategy = NoHeaderStrategy(),
                responseType = object : ParameterizedTypeReference<String>() {},
                getDataFromResponse = getAuthorizationTokenFromServerResponse)
                .execute(getUserDataFromUIComponents())
    }

    private fun getUserDataFromUIComponents(): LoginRequestBody =
            LoginRequestBody(
                    usernameEditTextFromLoginActivity.text.toString(),
                    passwordEditTextFromLoginActivity.text.toString())

    private fun saveTokenToSharedPreferences(token: String) =
            getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE).edit()
                    .let { it.putString(getString(R.string.token), token) }
                    .let { it.commit() }
}

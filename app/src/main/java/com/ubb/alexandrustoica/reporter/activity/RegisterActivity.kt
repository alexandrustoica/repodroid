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
import com.ubb.alexandrustoica.reporter.domain.RegisterRequestBody
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.rest.NoHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.OnAsyncResponseReadyCallback
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_register.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder

class RegisterActivity : AppCompatActivity(), OnAsyncResponseReadyCallback<String, String> {

    private val getAuthorizationTokenFromServerResponse:
            (ResponseEntity<String>) -> String = {
        it.headers["Authorization"]?.get(0) ?: ""
    }

    private val whenAuthorizationTokenIsReady: (String) -> Unit = {
        saveTokenToSharedPreferences(it).also {
            startActivity(Intent(this, ReportsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        listOf(usernameEditText, passwordEditText,
                nameEditText, emailEditText, confirmPasswordEditText).forEach {
            it.apply {
                pivotY = 200f
                scaleY = 0f
            }
        }
        AnimatorSet().also {
            it.playSequentially(
                    ObjectAnimator.ofFloat(confirmPasswordEditText, "scaleY", 0f, 1f),
                    ObjectAnimator.ofFloat(passwordEditText, "scaleY", 0f, 1f),
                    ObjectAnimator.ofFloat(emailEditText, "scaleY", 0f, 1f),
                    ObjectAnimator.ofFloat(usernameEditText, "scaleY", 0f, 1f),
                    ObjectAnimator.ofFloat(nameEditText, "scaleY", 0f, 1f))
        }.setDuration(200).start()
    }

    override fun onTaskCompleted(result: AsyncResponse<String, String>) {
        result.ifResult{ if(it != "") whenAuthorizationTokenIsReady }
                .ifError { showErrorMessageToUser(it) }
    }

    fun onRegisterButtonClick(view: View) {
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        if (password == confirmPassword) {
            requestToCreateNewUserOnServer()
            requestAuthorizationTokenFromServer()
        }
        else showErrorMessageToUser("Passwords don't match!")
    }

    private fun requestToCreateNewUserOnServer() =
        sendRequestToServerBasedOn(UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/users/register"),
                getUserDataFromUIComponents())

    private fun requestAuthorizationTokenFromServer() =
            sendRequestToServerBasedOn( UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/login"),
                    LoginRequestBody(username = usernameEditText.text.toString(),
                            password = passwordEditText.text.toString()))

    private fun sendRequestToServerBasedOn(url: UriComponentsBuilder, data: Any) {
        RestAsyncTask<Any, String>(
                context = this@RegisterActivity,
                method = HttpMethod.POST,
                urlBuilder = url,
                headerStrategy = NoHeaderStrategy(),
                responseType = object : ParameterizedTypeReference<String>() {},
                getDataFromResponse = getAuthorizationTokenFromServerResponse)
                .execute(data)
    }

    private fun showErrorMessageToUser(message: String) {
        BasicAlertDialog(this@RegisterActivity, message).show()
    }

    private fun getUserDataFromUIComponents(): RegisterRequestBody =
            RegisterRequestBody(
                    usernameEditText.text.toString(),
                    nameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString())

    private fun saveTokenToSharedPreferences(token: String) {
        getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE).edit()
                .let { it.putString(getString(R.string.token), token) }
                .let { it.commit() }
    }
}

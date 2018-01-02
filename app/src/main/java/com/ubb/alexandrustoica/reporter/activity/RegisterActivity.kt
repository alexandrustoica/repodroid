package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.domain.RegisterRequestBody
import com.ubb.alexandrustoica.reporter.rest.*
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
    }

    override fun onTaskCompleted(result: AsyncResponse<String, String>) {
        result.ifResult(whenAuthorizationTokenIsReady)
                .ifError { showErrorMessageToUser(it) }
    }

    fun onRegisterButtonClick(view: View) {
        val password = passwordEditTextRegister.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        if (password == confirmPassword)
            requestAuthorizationTokenFromServer()
        else showErrorMessageToUser("Passwords don't match!")
    }

    private fun requestAuthorizationTokenFromServer() {
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/users/register")
        RestAsyncTask<RegisterRequestBody, String>(
                context = this@RegisterActivity,
                method = HttpMethod.POST,
                urlBuilder = urlBuilder,
                headerStrategy = NoHeaderStrategy(),
                responseType = object : ParameterizedTypeReference<String>() {},
                getDataFromResponse = getAuthorizationTokenFromServerResponse)
                .execute(getUserDataFromUIComponents())
    }

    private fun showErrorMessageToUser(message: String) {
        BasicAlertDialog(this@RegisterActivity, message).show()
    }

    private fun getUserDataFromUIComponents(): RegisterRequestBody =
            RegisterRequestBody(
                    usernameEditTextRegister.text.toString(),
                    nameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditTextRegister.text.toString())

    private fun saveTokenToSharedPreferences(token: String) {
        getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE).edit()
                .let { it.putString(getString(R.string.token), token) }
                .let { it.commit() }
    }
}

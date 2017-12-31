package com.ubb.alexandrustoica.reporter.task

import android.content.Context
import android.os.AsyncTask
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.domain.AsyncResponse
import com.ubb.alexandrustoica.reporter.domain.LoginRequestBody
import org.springframework.http.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate


class LoginRequestTask(private val context: Context) :
        AsyncTask<LoginRequestBody, Void, AsyncResponse<String, String>>() {

    override fun doInBackground(vararg parameters: LoginRequestBody): AsyncResponse<String, String> {
        val url = "${context.getString(R.string.service_url_base)}/login"
        val restTemplate = RestTemplate()
                .also { it.messageConverters.add(MappingJackson2HttpMessageConverter()) }
        val headers = HttpHeaders()
                .also { it.contentType = MediaType.APPLICATION_JSON }
        val request = HttpEntity<LoginRequestBody>(parameters[0], headers)
        return try {
            val response = restTemplate.exchange(url, HttpMethod.POST, request, String::class.java)
            AsyncResponse(result = response.headers["Authorization"]?.get(0) ?: "")
        } catch (exception: Exception) {
            AsyncResponse(error = exception.message)
        }
    }

    override fun onPostExecute(result: AsyncResponse<String, String>) =
            (context as CallbackTask<AsyncResponse<String, String>>)
                    .onTaskCompleted(result)
}
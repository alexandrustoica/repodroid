package com.ubb.alexandrustoica.reporter.task

import android.content.Context
import android.os.AsyncTask
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.domain.AsyncResponse
import com.ubb.alexandrustoica.reporter.domain.RegisterRequestBody
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

class RegisterRequestTask(private val context: Context) :
        AsyncTask<RegisterRequestBody, Void, AsyncResponse<String, String>>() {

    override fun doInBackground(vararg parameters: RegisterRequestBody):
            AsyncResponse<String, String> {
        val url = "${context.getString(R.string.service_url_base)}/users/register"
        val restTemplate = RestTemplate()
                .also { it.messageConverters.add(MappingJackson2HttpMessageConverter()) }
        val headers = HttpHeaders()
                .also { it.contentType = MediaType.APPLICATION_JSON }
        val request = HttpEntity<RegisterRequestBody>(parameters[0], headers)
        return try {
            val response = restTemplate.exchange(url, HttpMethod.POST, request, String::class.java)
            AsyncResponse(result = response.headers["Authorization"]?.get(0) ?: "")
        } catch (exception: Exception) {
            AsyncResponse(error = exception.message)
        }
    }

    override fun onPostExecute(result: AsyncResponse<String, String>) =
            (context as CallbackAsyncResponse<String, String>).onTaskCompleted(result)
}
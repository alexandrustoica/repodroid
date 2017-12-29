package com.ubb.alexandrustoica.reporter.task

import android.content.Context
import android.os.AsyncTask
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.domain.RegisterRequestBody
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

class RegisterRequestTask(private val context: Context) :
        AsyncTask<RegisterRequestBody, Void, String>() {

    override fun doInBackground(vararg parameters: RegisterRequestBody): String {
        val url = "${context.getString(R.string.service_url_base)}/users/register"
        val body = LinkedMultiValueMap<String, String>()
        body.add("Content-Type", "application/json")
        val restTemplate = RestTemplate()
                .also { it.messageConverters.add(MappingJackson2HttpMessageConverter()) }
        val request = HttpEntity<RegisterRequestBody>(parameters[0], body)
        val response = restTemplate.exchange(
                url, HttpMethod.POST, request, String::class.java)
        return response.headers["Authorization"]?.get(0) ?: ""
    }

    override fun onPostExecute(result: String) =
        (context as CompletedTask<String>).onTaskCompleted(result)
}
package com.ubb.alexandrustoica.reporter.rest

import android.content.Context
import android.os.AsyncTask
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class RestAsyncTask<T, R>(
        private val context: Context,
        private val method: HttpMethod,
        private val urlBuilder: UriComponentsBuilder,
        private val headerStrategy: HeaderStrategy = NoHeaderStrategy(),
        private val responseType: ParameterizedTypeReference<R>,
        private val getDataFromResponse: (ResponseEntity<R>) -> R = { it.body }) :
        AsyncTask<T, Void, AsyncResponse<R, String>>() {

    private val basicHeaderStrategy = JsonContentHeaderStrategy(NoHeaderStrategy())

    private val restTemplate = RestTemplate()
            .also { it.messageConverters.add(MappingJackson2HttpMessageConverter()) }

    private val headers = HttpHeaders()

    override fun doInBackground(vararg parameters: T): AsyncResponse<R, String> {
        val request = HttpEntity<T>(parameters[0], basicHeaderStrategy.complete(headers)
                .also { headerStrategy.complete(it) })
        return try {
            val response = restTemplate.exchange(urlBuilder.build().encode().toUri(),
                    method, request, responseType)
            AsyncResponse(getDataFromResponse(response))
        } catch (exception: Exception) {
            AsyncResponse(error = exception.message)
        }
    }

    override fun onPostExecute(result: AsyncResponse<R, String>) =
            (context as CallbackTask<AsyncResponse<R, String>>)
                    .onTaskCompleted(result)
}
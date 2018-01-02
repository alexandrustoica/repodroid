package com.ubb.alexandrustoica.reporter.rest

import android.content.Context
import android.os.AsyncTask
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

data class RestAsyncTask<T, R>(
        private val context: Context,
        private val method: HttpMethod,
        private val urlBuilder: UriComponentsBuilder,
        private val headerStrategy: HeaderStrategy = NoHeaderStrategy(),
        private val responseType: ParameterizedTypeReference<R>,
        private val getDataFromResponse: (ResponseEntity<R>) -> R = { it.body }) :
        AsyncTask<T, Void, AsyncResponse<R, String>>() {

    private val mapper = ObjectMapper()
    private val basicHeaderStrategy =
            JsonContentHeaderStrategy(NoHeaderStrategy())
    private val restTemplate = RestTemplate()
            .also { it.messageConverters.add(MappingJackson2HttpMessageConverter()) }
    private val headers = HttpHeaders()

    override fun doInBackground(vararg parameters: T): AsyncResponse<R, String> {
        return try {
            val response = restTemplate.exchange(
                    urlBuilder.build().encode().toUri(),
                    method, getRequestFromParameters(parameters), responseType)
            AsyncResponse(getDataFromResponse(response))
        } catch (exception: HttpClientErrorException) {
            AsyncResponse(error = mapper.readTree(exception.responseBodyAsString)
                    .get("message").toString())
        }
    }

    private fun getRequestFromParameters(parameters: Array<out T>) =
            HttpEntity<T>(parameters.getOrElse(0, { null }),
                    basicHeaderStrategy.complete(headers)
                            .also { headerStrategy.complete(it) })


    override fun onPostExecute(result: AsyncResponse<R, String>) =
            (context as OnTaskResponseCompletedCallback<AsyncResponse<R, String>>)
                    .onTaskCompleted(result)
}
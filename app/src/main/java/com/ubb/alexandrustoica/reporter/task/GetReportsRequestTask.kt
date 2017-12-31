package com.ubb.alexandrustoica.reporter.task

import android.content.Context
import android.os.AsyncTask
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.activity.Report
import com.ubb.alexandrustoica.reporter.domain.AsyncResponse
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


class GetReportsRequestTask(private val token: String,
                            private val context: Context) :
        AsyncTask<Int, Void, AsyncResponse<List<Report>, String>>() {

    override fun doInBackground(vararg parameters: Int?):
            AsyncResponse<List<Report>, String> {
        val url = "${context.getString(R.string.service_url_base)}/reports"
        val builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("page", parameters[0])
                .queryParam("size", 10)
        val restTemplate = RestTemplate()
                .also { it.messageConverters.add(MappingJackson2HttpMessageConverter()) }
        val headers = HttpHeaders()
                .also { it.contentType = MediaType.APPLICATION_JSON }
                .also { it.set("Authorization", token) }
        val request = HttpEntity<Any>(headers)
        return try {
            val response = restTemplate.exchange(builder.build().encode().toUri(),
                    HttpMethod.GET, request, object: ParameterizedTypeReference<ArrayList<Report>>(){})
            AsyncResponse(response.body)
        } catch (exception: Exception) {
            AsyncResponse(error = exception.message)
        }
    }

    override fun onPostExecute(result: AsyncResponse<List<Report>, String>) =
            (context as CallbackTask<AsyncResponse<List<Report>, String>>)
                    .onTaskCompleted(result)
}
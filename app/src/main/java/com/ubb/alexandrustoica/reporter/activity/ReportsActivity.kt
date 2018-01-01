package com.ubb.alexandrustoica.reporter.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.domain.Report
import com.ubb.alexandrustoica.reporter.rest.AuthorizationHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.CallbackAsyncResponse
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.content_reports.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.atomic.AtomicInteger


class ReportsActivity : AppCompatActivity(),
        CallbackAsyncResponse<List<Report>, String> {

    private val currentPage: AtomicInteger = AtomicInteger(0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        setSupportActionBar(toolbar)
        reportsRecyclerView_reports.layoutManager =
                LinearLayoutManager(this@ReportsActivity)
        requestReportsFromServer()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun requestReportsFromServer() {
        val token = getSharedPreferences(getString(R.string.preferences_file_key),
                Context.MODE_PRIVATE).getString("token", "")
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/reports")
                .queryParam("page", currentPage.getAndIncrement())
                .queryParam("size", 10)

        RestAsyncTask<Unit, ArrayList<Report>>(
                this@ReportsActivity, HttpMethod.GET, urlBuilder,
                AuthorizationHeaderStrategy(token),
                object : ParameterizedTypeReference<ArrayList<Report>>() {})
                .execute(null)
    }

    override fun onTaskCompleted(result: AsyncResponse<List<Report>, String>) {
        println(result)
        result.ifResult { reportsRecyclerView_reports.adapter = ReportsRecyclerAdaptor(it) }
        result.ifError { BasicAlertDialog(this@ReportsActivity, it).show() }
    }
}

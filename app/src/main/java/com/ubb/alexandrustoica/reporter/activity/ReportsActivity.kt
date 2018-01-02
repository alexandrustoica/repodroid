package com.ubb.alexandrustoica.reporter.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.transition.Explode
import android.view.Menu
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.domain.PaginationRepository
import com.ubb.alexandrustoica.reporter.domain.Report
import com.ubb.alexandrustoica.reporter.domain.Repository
import com.ubb.alexandrustoica.reporter.domain.Token
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.rest.AuthorizationHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.OnAsyncResponseReadyCallback
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_reports.*
import kotlinx.android.synthetic.main.content_reports.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder

class ReportsActivity : AppCompatActivity(),
        OnAsyncResponseReadyCallback<List<Report>, String> {

    private var repository: Repository<Report> = PaginationRepository()
    private val adaptor = ReportsRecyclerAdaptor(this@ReportsActivity, listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        setSupportActionBar(toolbar)
        window.exitTransition = Explode()
        reportsRecyclerView_reports.layoutManager =
                LinearLayoutManager(this@ReportsActivity)
        addReportActionButton.setOnClickListener {
            startActivity(Intent(this, AddReviewActivity::class.java))
        }
        reportsRecyclerView_reports.adapter =
                adaptor.also { it.addOnBottomReachedListener { requestReportsFromServer() } }
        requestReportsFromServer()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            viewReportsGraphButton.setOnClickListener {
                startActivity(Intent(this@ReportsActivity,
                        GraphReportActivity::class.java))
            }.let { super.onCreateOptionsMenu(menu) }

    private fun requestReportsFromServer() {
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/reports")
                .queryParam("page", repository.currentPage())
                .queryParam("size", 20)
        RestAsyncTask<Unit, ArrayList<Report>>(
                context = this@ReportsActivity,
                method = HttpMethod.GET,
                urlBuilder = urlBuilder,
                headerStrategy = AuthorizationHeaderStrategy(Token(this@ReportsActivity)),
                responseType = object : ParameterizedTypeReference<ArrayList<Report>>() {})
                .execute()
    }

    private val onListReportsReadyLoadThem: (List<Report>) -> Unit = {
        repository = repository.addAllFrom(it)
        adaptor.updateData(repository.getAllDataFromRepository())
    }

    override fun onTaskCompleted(result: AsyncResponse<List<Report>, String>) {
        result.ifResult(onListReportsReadyLoadThem)
                .ifError { BasicAlertDialog(this@ReportsActivity, it).show() }
    }
}

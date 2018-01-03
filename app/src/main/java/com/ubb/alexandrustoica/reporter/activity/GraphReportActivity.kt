package com.ubb.alexandrustoica.reporter.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.domain.Report
import com.ubb.alexandrustoica.reporter.domain.Token
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.rest.AuthorizationHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.OnAsyncResponseReadyCallback
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_graph_report.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder
import java.text.SimpleDateFormat

class GraphReportActivity : AppCompatActivity(),
        OnAsyncResponseReadyCallback<List<Report>, String> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_report)
        requestReportsFromLatestWeekFromServer()
    }

    private fun requestReportsFromLatestWeekFromServer() {
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/reports/latest")
        RestAsyncTask<Void, ArrayList<Report>>(
                context = this@GraphReportActivity,
                method = HttpMethod.GET,
                urlBuilder = urlBuilder,
                headerStrategy = AuthorizationHeaderStrategy(Token(context = this).value),
                responseType = object : ParameterizedTypeReference<ArrayList<Report>>() {})
                .execute(null)
    }

    override fun onTaskCompleted(result: AsyncResponse<List<Report>, String>) {
        result.ifResult { buildGraphWith(it) }
        result.ifError { BasicAlertDialog(this, it) }
    }

    private fun buildGraphWith(reports: List<Report>) {
        val result = reports
                .groupBy { SimpleDateFormat("MM/dd/yyyy").format(it.date.time) }
                .map { it.value.size.toDouble() }.toList()
        val points: List<DataPoint> = result
                .mapIndexed { index, numberOfReports ->
                    listOf(DataPoint(index.toDouble(), numberOfReports),
                            DataPoint(index.toDouble() + 1, numberOfReports))
                }
                .flatten()
        val totalPoints: List<DataPoint> = points +
                generateSequence(points.size.toDouble(), { it + 1.0 })
                        .map { listOf(DataPoint(it, 0.0), DataPoint(it + 1, 0.0)) }
                        .take(7 - (points.size / 2)).flatten().toList()
        points.forEach { println(it) }
        val series = BarGraphSeries<DataPoint>(totalPoints.toTypedArray())
        (reportsGraph as GraphView).also { it.addSeries(series) }
    }
}

package com.ubb.alexandrustoica.reporter.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.domain.Report
import com.ubb.alexandrustoica.reporter.domain.Token
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.rest.AuthorizationHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.OnAsyncResponseReadyCallback
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_report.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder
import java.text.SimpleDateFormat


class ReportActivity :
        AppCompatActivity(),
        OnAsyncResponseReadyCallback<Report, String>,
        OnMapReadyCallback {

    private lateinit var report: Report

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        requestReportFromServer(intent.getIntExtra("id", 0))
        deleteActionButtonFromReportActivity.setOnClickListener {
            requestReportToBeDeletedFromServer(intent.getIntExtra("id", 0))
            startActivity(Intent(this@ReportActivity, ReportsActivity::class.java))
            finish()
        }
    }

    private fun requestReportToBeDeletedFromServer(id: Int) {
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/reports/$id")
        RestAsyncTask<Unit, Report>(
                context = this@ReportActivity,
                method = HttpMethod.DELETE,
                urlBuilder = urlBuilder,
                headerStrategy = AuthorizationHeaderStrategy(Token(this)),
                responseType = object : ParameterizedTypeReference<Report>() {})
                .execute(null)
    }

    private fun requestReportFromServer(id: Int) {
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/reports/$id")
        RestAsyncTask<Unit, Report>(
                context = this@ReportActivity,
                method = HttpMethod.GET,
                urlBuilder = urlBuilder,
                headerStrategy = AuthorizationHeaderStrategy(Token(this)),
                responseType = object : ParameterizedTypeReference<Report>() {})
                .execute(null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(report.location.latitude,
                report.location.longitude)
        googleMap.addMarker(MarkerOptions()
                .position(position)
                .title(report?.text))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16f))
        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onTaskCompleted(result: AsyncResponse<Report, String>) {
        result.ifResult { showDataOnUIComponents(it) }
                .ifError { BasicAlertDialog(this, it) }
    }

    private fun showDataOnUIComponents(report: Report) {
        this.report = report
        textTextView_reportActivity.text = report.text
        dateTextView_reportActivity.text =
                SimpleDateFormat("yyyy-MM-dd").format(report.date.time)
        (map as SupportMapFragment).getMapAsync(this)
    }
}

package com.ubb.alexandrustoica.reporter.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.location.LocationServices
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.domain.Location
import com.ubb.alexandrustoica.reporter.domain.Report
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.rest.AuthorizationHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.OnAsyncResponseReadyCallback
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_add_review.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder

class AddReviewActivity : AppCompatActivity(),
        OnAsyncResponseReadyCallback<Report, String> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
    }

    @SuppressLint("MissingPermission")
    fun onAddReportClick(view: View) {
        val location = LocationServices.getFusedLocationProviderClient(this)
        requestPermissions(arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION), 101)
        val last = location.lastLocation
        val token = getSharedPreferences(getString(R.string.preferences_file_key),
                Context.MODE_PRIVATE).getString("token", "")
        last.addOnSuccessListener {
            if (it != null) {
                val urlBuilder = UriComponentsBuilder
                        .fromHttpUrl("${getString(R.string.service_url_base)}/reports")
                RestAsyncTask<Report, Report>(
                        this@AddReviewActivity, HttpMethod.POST, urlBuilder,
                        AuthorizationHeaderStrategy(token), object : ParameterizedTypeReference<Report>() {})
                        .execute(Report(
                                0, textEditText_addReportActivity.text.toString(), Location(it.latitude, it.longitude)))
            }
        }.addOnFailureListener { println(it) }
    }

    override fun onTaskCompleted(result: AsyncResponse<Report, String>) {
        result.ifResult { startActivity(Intent(this, ReportsActivity::class.java)) }
        result.ifError { println(it) }
    }
}

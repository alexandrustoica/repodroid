package com.ubb.alexandrustoica.reporter.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ubb.alexandrustoica.reporter.R
import com.ubb.alexandrustoica.reporter.components.BasicAlertDialog
import com.ubb.alexandrustoica.reporter.domain.Location
import com.ubb.alexandrustoica.reporter.domain.Report
import com.ubb.alexandrustoica.reporter.domain.Token
import com.ubb.alexandrustoica.reporter.rest.AsyncResponse
import com.ubb.alexandrustoica.reporter.rest.AuthorizationHeaderStrategy
import com.ubb.alexandrustoica.reporter.rest.OnAsyncResponseReadyCallback
import com.ubb.alexandrustoica.reporter.rest.RestAsyncTask
import kotlinx.android.synthetic.main.activity_add_review.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.util.UriComponentsBuilder

class AddReviewActivity : AppCompatActivity(),
        LocationListener,
        OnMapReadyCallback,
        OnAsyncResponseReadyCallback<Report, String> {

    @SuppressLint("MissingPermission")
    private fun lastLocation() = LocationServices
            .getFusedLocationProviderClient(this).lastLocation

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        (mapViewForCurrentLocation as SupportMapFragment).getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.also { it.uiSettings.isZoomGesturesEnabled = true }
                .also { it.isMyLocationEnabled = true }
        lastLocation().addOnSuccessListener { setNewMarkerForCurrentLocationOnMaps(it) }
    }

    override fun onLocationChanged(lastLocation: android.location.Location?) =
            setNewMarkerForCurrentLocationOnMaps(lastLocation)

    private fun setNewMarkerForCurrentLocationOnMaps(
            location: android.location.Location?) {
        val position = LatLng(
                location?.latitude ?: 0.0,
                location?.longitude ?: 0.0)
        map.addMarker(MarkerOptions().position(position))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16f))
    }

    fun onBackButtonClick(view: View) = finish()

    fun onAddReportClick(view: View) = lastLocation().addOnSuccessListener {
        sendRequestToAddReportToServer(it)
        setNewMarkerForCurrentLocationOnMaps(it)
    }

    private fun sendRequestToAddReportToServer(location: android.location.Location) {
        val urlBuilder = UriComponentsBuilder
                .fromHttpUrl("${getString(R.string.service_url_base)}/reports")
        RestAsyncTask<Report, Report>(
                context = this@AddReviewActivity,
                method = HttpMethod.POST,
                urlBuilder = urlBuilder,
                headerStrategy = AuthorizationHeaderStrategy(Token(this)),
                responseType = object : ParameterizedTypeReference<Report>() {})
                .execute(getReportFromUIComponents(location))
    }

    private fun getReportFromUIComponents(location: android.location.Location) =
            Report(0, reportTextEditText.text.toString(),
                    Location(location.latitude, location.longitude))

    override fun onTaskCompleted(result: AsyncResponse<Report, String>) {
        result.ifResult { startActivity(Intent(this, ReportsActivity::class.java)) }
        result.ifError { BasicAlertDialog(this@AddReviewActivity, it).show() }
    }
}

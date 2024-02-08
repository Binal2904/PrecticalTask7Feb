package com.demo.advanced.practical.view.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.demo.advanced.practical.R
import com.demo.advanced.practical.databinding.ActivityMapsViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsViewActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var locationManager: LocationManager

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 100
        private const val MIN_TIME_BW_UPDATES: Long = 1000 // 1 second
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 1f // 1 meter
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        handleToolbar()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            requestLocationUpdates()
        }
    }

    private fun handleToolbar() {
        binding.includeLayToolbar.imgBack.visibility = View.VISIBLE
        binding.includeLayToolbar.ivMap.visibility = View.GONE
        binding.includeLayToolbar.tvTitle.text = "Map"
        binding.includeLayToolbar.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            locationListener
        )
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME_BW_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            locationListener
        )
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false).setPositiveButton(
                "Yes"
            ) { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Handle location updates
            drawMarker(location)
        }

        override fun onProviderDisabled(provider: String) {
            // Handle when the provider is disabled
        }

        override fun onProviderEnabled(provider: String) {
            // Handle when the provider is enabled
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // Handle when the provider status changes
        }
    }

    private fun drawMarker(location: Location) {
        if (this::mMap.isInitialized) {
            if (mMap != null) {
                mMap.clear()
                val gps = LatLng(location.latitude, location.longitude)
                mMap.addMarker(
                    MarkerOptions().position(gps).title("Current Position")
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12f))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                // Handle the case where the user denied the permission
                Toast.makeText(
                    applicationContext,
                    "Please enable permission go to setting and enable",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
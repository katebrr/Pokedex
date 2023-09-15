//package com.katebrr.pokedex.features.pokemon
//
//
//import android.Manifest
//import android.app.Service
//import android.content.Intent
//import android.content.pm.PackageManager
//
//import android.os.IBinder
//import android.location.Location
//import android.os.Looper
//import androidx.core.app.ActivityCompat
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import javax.inject.Inject
//
//
//class MyNavigationService @Inject
//constructor() : Service() {
//    var coord = Coord(latitude = 0.0, longitude = 0.0)
//
//    lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    // Function to initialize the FusedLocationProviderClient
//    fun initializeLocationClient() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//    }
//
//    fun requestLatestLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            //
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                val latestLocation: Location? = locationResult.lastLocation
//                latestLocation?.let { location ->
//                    // Save the latest location to the Pokemon object
//                    coord.latitude = location.latitude
//                    coord.longitude = location.longitude
//                    // Stop receiving location updates
//                    fusedLocationClient.removeLocationUpdates(this)
//                    stopSelf()
//                }
//            }
//        }
//        val locationRequest = LocationRequest.create().apply {
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//            interval = 10000 // Interval in milliseconds for receiving location updates
//        }
//
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//    }
//
//
//    // Function to get the latest location from your service
//      fun getLocationFromService() : Coord? {
//        initializeLocationClient()
//        requestLatestLocation()
//        return (coord)
//    }
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//}
//
//data class Coord(
//    var latitude: Double,
//    var longitude: Double
//)
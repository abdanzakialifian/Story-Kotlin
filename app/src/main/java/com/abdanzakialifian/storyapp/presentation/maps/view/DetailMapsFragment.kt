package com.abdanzakialifian.storyapp.presentation.maps.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentDetailMapsBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.maps.model.DataLocation
import com.abdanzakialifian.storyapp.presentation.upload.view.UploadFragment.Companion.DATA_LOCATION
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class DetailMapsFragment : BaseVBFragment<FragmentDetailMapsBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun getViewBinding(): FragmentDetailMapsBinding =
        FragmentDetailMapsBinding.inflate(layoutInflater)

    override fun setupView() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.detail_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMyLocation()
//        requestLocationUpdates()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getMyLocation()
//                requestLocationUpdates()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                getDataLocation(location)
            }
        }
    }

    private fun getDataLocation(currentLocation: Location) {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val address =
            geoCoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 3)[0]

        binding.tvStreetName.text = address.thoroughfare
        binding.tvCompleteAddress.text =
            StringBuilder().append(address.thoroughfare).append(", ").append(address.subLocality)
                .append(", ").append(address.locality).append(", ").append(address.subAdminArea)
                .append(", ").append(address.adminArea).append(", ").append(address.countryName)

        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        mMap.addMarker(MarkerOptions().position(latLng).title(address.thoroughfare))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12F))

        val dataLocation = DataLocation(
            latitude = currentLocation.latitude,
            longitude = currentLocation.longitude,
            thoroughfare = address.thoroughfare
        )

        binding.btnOk.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                DATA_LOCATION,
                dataLocation
            )
            findNavController().popBackStack()
        }
    }

//    private fun isMockLocationOn(location: Location, context: Context): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            location.isFromMockProvider
//        } else {
//            Settings.Secure.getString(
//                context.contentResolver,
//                Settings.Secure.ALLOW_MOCK_LOCATION
//            ).equals("0")
//        }
//    }
//
//    private val mCallBack = object: LocationCallback(){
//        override fun onLocationResult(location: LocationResult) {
//            currentLocation = location.lastLocation!!
//            getDataLocation()
//            if (isMockLocationOn(location.lastLocation!!, requireContext())) Toast.makeText(
//                requireContext(),
//                "Mock active",
//                Toast.LENGTH_SHORT
//            ).show() else Toast.makeText(requireContext(), "Mock disabled", Toast.LENGTH_SHORT)
//                .show()
//        }
//
//        override fun onLocationAvailability(p0: LocationAvailability) {
//            Log.d(TAG, "onLocationAvailability: $p0")
//            super.onLocationAvailability(p0)
//        }
//
//    }
//
//    private fun createLocationRequest() = LocationRequest.create().apply {
//        interval = UPDATE_INTERVAL_IN_MILLISECONDS
//        fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
//        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//    }
//
//    @SuppressLint("MissingPermission")
//    fun requestLocationUpdates() {
//        try {
//            fusedLocationProviderClient.requestLocationUpdates(
//                createLocationRequest(),
//                mCallBack, Looper.myLooper()
//            )
//        } catch (ex: SecurityException) {
//            Log.e(TAG, "Lost location permission. Could not request updates. $ex")
//        }
//    }
//
//    private fun removeLocationUpdates() {
//        try {
//            fusedLocationProviderClient.removeLocationUpdates(mCallBack)
//
//        } catch (ex : SecurityException) {
//            Log.e(TAG, "Lost location permission. Could not remove updates. $ex")
//        }
//    }
//
//    override fun onDestroy() {
//        removeLocationUpdates()
//        super.onDestroy()
//    }

//    companion object{
//        private const val UPDATE_INTERVAL_IN_MILLISECONDS = 10000L
//        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
//            UPDATE_INTERVAL_IN_MILLISECONDS / 2
//        private val TAG = DetailMapsFragment::class.java.simpleName
//    }
}
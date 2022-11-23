package com.abdanzakialifian.storyapp.presentation.maps.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentMapsBinding
import com.abdanzakialifian.storyapp.domain.model.Stories
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.maps.viewmodel.MapsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsFragment : BaseVBFragment<FragmentMapsBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val viewModel by viewModels<MapsViewModel>()

    override fun getViewBinding(): FragmentMapsBinding = FragmentMapsBinding.inflate(layoutInflater)

    override fun setupView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        setCustomMaps()
        getLocation()
    }

    private fun setCustomMaps() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.custom_map
                    )
                )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Exception) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun getLocation() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiStateLocationUser
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { listStory ->
                    listStory.forEach { story ->
                        setMapsMarker(story)
                    }
                }
        }
    }

    private fun setMapsMarker(story: Stories) {
        Glide.with(this@MapsFragment)
            .asBitmap()
            .circleCrop()
            .apply(RequestOptions().override(100, 100))
            .load(story.photoUrl)
            .into(object : CustomTarget<Bitmap>(25, 25) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
                    mMap.addMarker(
                        MarkerOptions().position(latLng).title(story.name)
                            .snippet(story.description)
                            .icon(BitmapDescriptorFactory.fromBitmap(resource))
                    )
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8F))
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    companion object {
        private val TAG = MapsFragment::class.java.simpleName
    }
}
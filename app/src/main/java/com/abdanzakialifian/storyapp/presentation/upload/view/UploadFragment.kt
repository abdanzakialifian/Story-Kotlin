package com.abdanzakialifian.storyapp.presentation.upload.view

import android.net.Uri
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentUploadBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.maps.model.DataLocation
import com.abdanzakialifian.storyapp.presentation.upload.viewmodel.UploadViewModel
import com.abdanzakialifian.storyapp.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class UploadFragment : BaseVBFragment<FragmentUploadBinding>(), OnMapReadyCallback {

    private val args by navArgs<UploadFragmentArgs>()
    private val viewModel by viewModels<UploadViewModel>()
    private lateinit var imageStringToUri: Uri
    private lateinit var mMap: GoogleMap
    private var dataLocation: DataLocation? = null

    override fun getViewBinding(): FragmentUploadBinding =
        FragmentUploadBinding.inflate(layoutInflater)

    override fun setupView() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        val imageString = args.imageString
        // convert String to Uri
        imageStringToUri = Uri.parse(imageString)
        binding.imgPicture.setImageURI(imageStringToUri)
        binding.btnUpload.setOnClickListener {
            uploadNewStory()
        }

        // configure google maps
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.small_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<DataLocation>(
            DATA_LOCATION
        )?.observe(viewLifecycleOwner) { dataLocation -> this.dataLocation = dataLocation }
    }

    private fun uploadNewStory() {
        viewModel.getToken()

        // convert Uri to File
        val imageUriToFile = uriToFile(imageStringToUri, requireContext())

        // image compress
        val reduceFile = reduceFileImage(imageUriToFile)

        val description =
            binding.edtDescription.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val latitude = if (dataLocation?.latitude != null) dataLocation?.latitude.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull()) else null
        val longitude = if (dataLocation?.longitude != null) dataLocation?.longitude.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull()) else null

        val requestImageFile = reduceFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            reduceFile.name,
            requestImageFile
        )

        lifecycleScope.launchWhenStarted {
            viewModel.getToken
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { token ->
                    viewModel.newStory(imageMultipart, description, latitude, longitude, token)
                }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiStateNewStory
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.apply {
                                layoutLoading.visible()
                                btnUpload.text = ""
                            }
                        }
                        Status.SUCCESS -> {
                            binding.apply {
                                layoutLoading.gone()
                                btnUpload.text = resources.getString(R.string.upload)
                            }
                            setAlertDialog()
                        }
                        Status.ERROR -> {
                            binding.apply {
                                layoutLoading.gone()
                                btnUpload.text = resources.getString(R.string.upload)
                            }
                            Toast.makeText(requireContext(), resources.getString(R.string.failed_upload), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }
    }

    private fun setAlertDialog() {
        val builder = AlertDialog.Builder(requireContext()).create()
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val view = layoutInflater.inflate(R.layout.custom_alert_dialog_success_upload, null)
        val btnOk = view.findViewById<Button>(R.id.btn_ok)
        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)
        btnOk.setOnClickListener {
            val actionToHomeFragment =
                UploadFragmentDirections.actionUploadFragmentToHomeFragment()
            findNavController().navigate(actionToHomeFragment)
            builder.dismiss()
        }
        builder.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (dataLocation != null) {
            val latLng = LatLng(dataLocation?.latitude ?: 0.0, dataLocation?.longitude ?: 0.0)
            mMap.addMarker(MarkerOptions().position(latLng).title(dataLocation?.thoroughfare))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
            binding.layoutMap.strokeColor =
                ContextCompat.getColor(requireContext(), R.color.light_purple)
        } else {
            binding.layoutMap.strokeColor = ContextCompat.getColor(requireContext(), R.color.grey)
        }

        mMap.setOnMapClickListener {
            val actionToDetailMapsFragment =
                UploadFragmentDirections.actionUploadFragmentToDetailMapsFragment()
            findNavController().navigate(actionToDetailMapsFragment)
        }
    }

    companion object {
        const val DATA_LOCATION = "Data Location"
    }
}
package com.abdanzakialifian.storyapp.presentation.camera.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentCameraBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraFragment : BaseVBFragment<FragmentCameraBinding>() {

    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageString: String? = null

    override fun getViewBinding(): FragmentCameraBinding =
        FragmentCameraBinding.inflate(layoutInflater)

    override fun setupView() {
        requestPermissionCamera()
        switchCamera()
        binding.apply {
            imgGallery.setOnClickListener {
                takePhotoFromGallery()
            }
            imgCapture.setOnClickListener {
                takePhoto()
            }
            btnBack.setOnClickListener {
                imgResult.gone()
                viewFinder.visible()
                imgGallery.visible()
                imgCapture.visible()
                imgSwitch.visible()
                layoutBottom.gone()
            }
            btnNext.setOnClickListener {
                val actionToUploadFragment =
                    CameraFragmentDirections.actionCameraFragmentToUploadFragment(imageString ?: "")
                findNavController().navigate(actionToUploadFragment)
            }
        }
    }

    private fun switchCamera() {
        binding.imgSwitch.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    // check request permission
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.camera_not_allowed),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            }
        }

    private fun requestPermissionCamera() {
        requestPermission.launch(Manifest.permission.CAMERA)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    requireContext().resources.getString(R.string.camera_fail),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(requireActivity().application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val resultPhoto = rotateBitmap(
                        BitmapFactory.decodeFile(photoFile.path),
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )

                    // convert bitmap to file
                    val bitmapToFile =
                        bitmapToFile(resultPhoto, photoFile)

                    // convert file to uri
                    imageString = Uri.fromFile(bitmapToFile).toString()

                    binding.apply {
                        imgResult.setImageBitmap(resultPhoto)
                        imgResult.visible()
                        viewFinder.gone()
                        imgGallery.gone()
                        imgCapture.gone()
                        imgSwitch.gone()
                        layoutBottom.visible()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.failed_take_pictures),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    // intent launcher gallery
    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImage = result?.data?.data as Uri
                imageString = selectedImage.toString()
                binding.apply {
                    imgResult.setImageURI(selectedImage)
                    imgResult.visible()
                    viewFinder.gone()
                    imgGallery.gone()
                    imgCapture.gone()
                    imgSwitch.gone()
                    layoutBottom.visible()
                }
            }
        }

    // take a photo from gallery device
    private fun takePhotoFromGallery() {
        val intent = Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }
}
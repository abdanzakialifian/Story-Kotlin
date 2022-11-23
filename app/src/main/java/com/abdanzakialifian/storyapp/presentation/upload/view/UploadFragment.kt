package com.abdanzakialifian.storyapp.presentation.upload.view

import android.net.Uri
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentUploadBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.upload.viewmodel.UploadViewModel
import com.abdanzakialifian.storyapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class UploadFragment : BaseVBFragment<FragmentUploadBinding>() {

    private val args by navArgs<UploadFragmentArgs>()
    private val viewModel by viewModels<UploadViewModel>()
    private lateinit var imageStringToUri: Uri

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
    }

    private fun uploadNewStory() {
        // convert Uri to File
        val imageUriToFile = uriToFile(imageStringToUri, requireContext())

        // image compress
        val reduceFile = reduceFileImage(imageUriToFile)

        val description =
            binding.edtDescription.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val requestImageFile = reduceFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            reduceFile.name,
            requestImageFile
        )

        lifecycleScope.launchWhenStarted {
            viewModel.newStory(imageMultipart, description)
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
}
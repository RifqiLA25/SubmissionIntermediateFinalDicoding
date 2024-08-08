package com.example.submissionawal.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.submissionawal.R
import com.example.submissionawal.data.remote.pref.UserPreference
import com.example.submissionawal.data.remote.response.UploadResponse
import com.example.submissionawal.databinding.ActivityAddStoryBinding
import com.example.submissionawal.help.reduceFileImage
import com.example.submissionawal.help.uriToFile
import com.example.submissionawal.ui.view.CameraActivity.Companion.RESULT_CAMERAX
import com.example.submissionawal.ui.viewmodel.AddStoryViewModel
import com.example.submissionawal.ui.viewmodelfactory.ViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityAddStoryBinding

    private var currentImageUri: Uri? = null

    private var currentLocation: Location? = null

    private lateinit var locationManager: LocationManager

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.title_story)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.buttonCameraX.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        binding.checkBoxLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                } else {
                    getCurrentLocation()
                }
            } else {
                currentLocation = null
            }
        }
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_CAMERAX) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImage.setImageURI(it)
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                currentLocation = it
            }
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.EditText.text.toString()
            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {
                    val userPreference = UserPreference.getInstance(this@AddStoryActivity)
                    val token = userPreference.getSession().first().token
                    val lat = currentLocation?.latitude
                    val lon = currentLocation?.longitude

                    viewModel.uploadStoryImage(token, multipartBody, requestBody, lat, lon)

                    viewModel.response.observe(this@AddStoryActivity) { res ->
                        if (!res.error) {
                            AlertDialog.Builder(this@AddStoryActivity).apply {
                                setTitle(getString(R.string.succes))
                                setMessage(getString(R.string.img_upload_succes))
                                setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                                    dialog.dismiss()
                                    clearInputFields()
                                    showLoading(false)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                    }
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                    AlertDialog.Builder(this@AddStoryActivity).apply {
                        setTitle(getString(R.string.succes))
                        setMessage(getString(R.string.failed) + ": ${errorResponse.message}")
                        setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                            showLoading(false)
                        }
                        create()
                        show()
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        showLoading(false)
    }

    private fun clearInputFields() {
        binding.EditText.text = null
        binding.previewImage.setImageURI(null)
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val PHOTO_PICKER = "Photo Picker"
    }
}
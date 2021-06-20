package com.example.hackatonapp.presentation.screen.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentCameraBinding
import com.example.hackatonapp.presentation.extensions.viewBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val binding by viewBinding(FragmentCameraBinding::bind)

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = this.requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else this.requireActivity().filesDir
    }

    fun finalDataCheck(values: ArrayList<ArrayList<String>>) : ArrayList<Int> {
        val minPulseValue = 40
        val minSys = 90
        val minDia = 40
        // have catchced whole block
        val ansver = ArrayList<Int>()
        if (values.isNotEmpty()) {
            if (values.size == 1 && values[0].size == 3) {
                // have lost '1'
                if (values[0][0].toInt() < minSys && values[0][0].length == 2) {
                    ansver.add(100 + values[0][0].toInt())
                } else {
                    ansver.add(values[0][0].toInt())
                }
                if (ansver[ansver.size - 1] < minSys) ansver[ansver.size - 1] = -1
                if (ansver[ansver.size - 1] > values[0][1].toInt() &&
                    values[0][1].toInt() >= minDia
                ) {
                    ansver.add(values[0][1].toInt())
                } else ansver.add(-1)

                if (values[0][2].toInt() >= minPulseValue) ansver.add(values[0][2].toInt())
                else ansver.add(-1)
                return ansver
            } else {
                if (values.size >= 2) {
                    if (values[0].size == 1 && values[1].size == 2) {
                        if (values[0][0].toInt() < minSys && values[0][0].length == 2) {
                            ansver.add(100 + values[0][0].toInt())
                        } else {
                            ansver.add(values[0][0].toInt())
                        }
                        if (ansver[ansver.size - 1] < minSys) ansver[ansver.size - 1] = -1
                        if (values[1][0].toInt() >= minDia) ansver.add(values[1][0].toInt())
                        else ansver.add(-1)
                        if (values[1][1].toInt() >= minPulseValue) ansver.add(values[1][0].toInt())
                        else ansver.add(-1)
                    }
                    else if (values[0].size == 2 && values[1].size == 1) {
                        if (values[0][0].toInt() < minSys && values[0][0].length == 2) {
                            ansver.add(100 + values[0][0].toInt())
                        } else {
                            ansver.add(values[0][0].toInt())
                        }
                        if (ansver[ansver.size - 1] < minSys) ansver[ansver.size - 1] = -1
                        if (values[0][1].toInt() >= minDia) ansver.add(values[0][1].toInt())
                        else ansver.add(-1)
                        if (values[1][0].toInt() >= minPulseValue) ansver.add(values[1][0].toInt())
                        else ansver.add(-1)
                    }
                    else {
                        if (values[0][0].toInt() < minSys && values[0][0].length == 2) {
                            ansver.add(100 + values[0][0].toInt())
                        } else {
                            ansver.add(values[0][0].toInt())
                        }
                        if (ansver[ansver.size - 1] < minSys) ansver[ansver.size - 1] = -1
                        if (values[1][0].toInt() >= minDia) ansver.add(values[1][0].toInt())
                        else ansver.add(-1)
                        if (values[2][0].toInt() >= minPulseValue) ansver.add(values[2][0].toInt())
                        else ansver.add(-1)
                    }
                }
            }
        }

        binding.cameraCaptureButton.isClickable = true
        saveToSharedPreferences(ansver)
        return ansver
    }

    private fun saveToSharedPreferences(token: ArrayList<Int>) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("SYS", token[0].toString())
            putString("DIA", token[1].toString())
            putString("Pulse", token[2].toString())
            apply()
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        binding.cameraCaptureButton.isClickable = false
//      val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(ContextCompat.getMainExecutor(this.requireContext()), object :
            ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
                override fun onCaptureSuccess(imageP: ImageProxy) {
                    val recognitionObject = DigitRecognition().apply {
                        onSuccessRecognitionListener = DigitRecognition.OnSuccessRecognitionListener {

                            var finalList = ArrayList<ArrayList<String>>()
                            for (block in it) {
                                if (block.size <= 3) {
                                    if (block.size == 3) {
                                        finalList.clear()
                                        finalList.add(block)
                                        break
                                    }
                                    finalList.add(block)
                                }
                            }
                            val ans = finalDataCheck(finalList)

                            for (el in ans) {
                                Toast.makeText(requireContext(), el.toString() + '\n', Toast.LENGTH_LONG).show()
                            }
                            findNavController().popBackStack()
                        }
                    }
                    try {
                        recognitionObject.analyze(imageP)
                        if (recognitionObject.image != null) {
                            recognitionObject.recognizeText()
                        }
                    } catch (e: Exception) {
                    }
                }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this.requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        startCamera()

        this.binding.cameraCaptureButton.setOnClickListener { takePhoto() }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(this.binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this.requireContext()))
    }
}
package com.example.hackatonapp.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.Fragment
import com.example.hackatonapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.google.android.gms.tasks.Task

import androidx.annotation.NonNull


class DigitRecognitioin : ImageAnalysis.Analyzer, AppCompatActivity() {

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        }
    }

    private fun processTextBlock(result: Text) {
        // [START mlkit_process_text_block]
        val resultText = result.text
        for (block in result.textBlocks) {
            val blockText = block.text
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    print(elementText)
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox
                }
                println()
            }
        }
        // [END mlkit_process_text_block]
    }

    private fun recognizeText(image: InputImage) {

        // [START get_detector_default]
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        // [END get_detector_default]

        // [START run_detector]
        val result = recognizer.process(image)
            .addOnSuccessListener {
                print("Detection successful")

            }
            .addOnFailureListener { e ->
                println("An error has occurred")
            }
            .addOnCompleteListener { task_ ->
                if (task_.isSuccessful) {
                    print("Text Detected")
                } else {
                    print("Text Detection Failed")
                }
            }
        // [END run_detector]
    }

    private fun getTextRecognizer(): TextRecognizer {
        // [START mlkit_local_doc_recognizer]
        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        // [END mlkit_local_doc_recognizer]
    }
}

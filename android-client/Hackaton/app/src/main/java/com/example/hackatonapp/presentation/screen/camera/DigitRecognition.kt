package com.example.hackatonapp.presentation.screen.camera

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import com.google.android.gms.tasks.Task
import kotlin.arrayOf as arrayOf


class DigitRecognition : ImageAnalysis.Analyzer {

    fun interface OnSuccessRecognitionListener {
        fun onSuccess(inputStr: ArrayList<ArrayList<String>>)
    }

    lateinit var onSuccessRecognitionListener: OnSuccessRecognitionListener

    var image: InputImage? = null
    var res: Task<Text>? = null

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        }
        imageProxy.close()
    }

    fun defineUnoNumber(number : String) : String? {
        val regExp = Regex("[^0-9]")
        val possibleMiss5 = """([sS])""".toRegex()
        val possibleMiss1 = """([iI|])""".toRegex()
        val possibleMiss0 = """([oO])""".toRegex()
        val possibleMiss8 = "B".toRegex()
        var res = possibleMiss5.replace(number, "5")
        res = possibleMiss1.replace(res, "1")
        res = possibleMiss0.replace(res, "0")
        res = possibleMiss8.replace(res, "8")
        return if (!regExp.containsMatchIn(res) && res.length <= 3 && res.length >= 2) res else null
    }
    fun defineNumbers(textBlocks : List<Text.TextBlock>) : ArrayList<ArrayList<String>> {
        var possibleValues = ArrayList<ArrayList<String>>()
        for (block in textBlocks) {
            var localElems = ArrayList<String>()
            for ((lnInd, line) in block.lines.withIndex()) {
                for ((elInd, elem) in line.elements.withIndex()) {
                    var result = defineUnoNumber(elem.text)
                    if (result != null) localElems.add(result)
                    if ( lnInd == block.lines.size - 1 &&
                            elInd == line.elements.size - 1 &&
                            localElems.isNotEmpty()) {
                        possibleValues.add(localElems)
                    }
                }
            }
        }
        return  possibleValues
    }

    fun recognizeText() {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val blocks: List<Text.TextBlock> = visionText.textBlocks
                if (blocks.isNotEmpty()) {
                    onSuccessRecognitionListener.onSuccess(defineNumbers(blocks))
                }
            }
    }
}

package com.dicoding.dapurnusantara.scan

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dapurnusantara.databinding.ActivityResultBinding
import com.dicoding.dapurnusantara.help.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))

        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)

            imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {

                    override fun onError(error: String) {
                        runOnUiThread{
                            Toast.makeText(this@ResultActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onResults(results: List<Classifications>?) {
                        runOnUiThread{

                            results?.firstOrNull()?.let { classification ->
                                if (classification.categories.isNotEmpty()) {
                                    val topCategory = classification.categories.first()
                                    val displayResult = "${topCategory.label} " + NumberFormat.getPercentInstance()
                                        .format(topCategory.score).trim()
                                    binding.resultText.text = displayResult
                                    Log.d(ContentValues.TAG, "Display result: $displayResult")
                                }
                            }
                        }
                    }
                }
            )

            imageClassifierHelper.classifyStaticImage(imageUri, contentResolver)
        }
    }


    companion object {
        val CAMERA_X_RESULT = 200
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }

}
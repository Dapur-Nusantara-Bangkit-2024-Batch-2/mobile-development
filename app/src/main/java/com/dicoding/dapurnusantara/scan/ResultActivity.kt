package com.dicoding.dapurnusantara.scan

import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dapurnusantara.databinding.ActivityResultBinding
import com.dicoding.dapurnusantara.help.ImageClassifierHelper

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }

}
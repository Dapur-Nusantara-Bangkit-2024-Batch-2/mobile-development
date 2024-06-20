package com.dicoding.dapurnusantara.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dapurnusantara.R

class ScanDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PREDICTED_LABEL = "extra_predicted_label"
    }

    private lateinit var imageView: ImageView
    private lateinit var detailresView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_detail)

        imageView = findViewById(R.id.detailImageView)
        detailresView = findViewById(R.id.detailResView)

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val label = intent.getStringExtra(EXTRA_PREDICTED_LABEL)

        if (!imageUri.isNullOrEmpty()) {
            loadImageFromUri(Uri.parse(imageUri))
        }

        if (!label.isNullOrEmpty()) {
            detailresView.text = label
        }
    }

    private fun loadImageFromUri(uri: Uri) {
        imageView.setImageURI(uri)
    }
}



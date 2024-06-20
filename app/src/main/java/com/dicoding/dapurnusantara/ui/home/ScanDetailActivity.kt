package com.dicoding.dapurnusantara.ui.home

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dapurnusantara.R

class ScanDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE_BITMAP = "extra_image_bitmap"
        const val EXTRA_PREDICTED_LABEL = "extra_predicted_label"
    }

    private lateinit var imageView: ImageView
    private lateinit var detailresView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_detail)

        imageView = findViewById(R.id.detailImageView)
        detailresView = findViewById(R.id.detailResView)

        val bitmap = intent.getParcelableExtra<Bitmap>(EXTRA_IMAGE_BITMAP)
        val label = intent.getStringExtra(EXTRA_PREDICTED_LABEL)

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        }

        if (!label.isNullOrEmpty()) {
            detailresView.text = label
        }
    }
}


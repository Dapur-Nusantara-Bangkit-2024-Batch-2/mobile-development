package com.dicoding.dapurnusantara.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.dapurnusantara.databinding.FragmentScanBinding
import com.dicoding.dapurnusantara.ml.Model3EfficientnetMetadata
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var bitmap: Bitmap? = null
    private lateinit var labels: List<String>
    private lateinit var imageProcessor: ImageProcessor
    private val CAMERA_REQUEST_CODE = 101
    private val CAMERA_PERMISSION_CODE = 102

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val view = binding.root

        labels = requireActivity().assets.open("labels.txt").bufferedReader().readLines()

        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        binding.selectBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.cameraBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            } else {
                openCamera()
            }
        }

        binding.predictBtn.setOnClickListener {
            if (bitmap == null) {
                Toast.makeText(requireActivity(), "Select image first", Toast.LENGTH_SHORT).show()
            } else {
                predictImage()
                val imageUri = saveBitmapToFile(bitmap!!, requireActivity())

                if (imageUri != null) {
                    val intent = Intent(requireActivity(), ScanDetailActivity::class.java).apply {
                        putExtra(ScanDetailActivity.EXTRA_IMAGE_URI, imageUri.toString())
                        putExtra(
                            ScanDetailActivity.EXTRA_PREDICTED_LABEL,
                            binding.resView.text.toString()
                        )
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(requireActivity(), "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun predictImage() {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap!!)

        val processedTensorImage = imageProcessor.process(tensorImage)

        val model = Model3EfficientnetMetadata.newInstance(requireActivity())

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(processedTensorImage.buffer)

        val outputs = model.process(inputFeature0)
        val probability = outputs.probabilityAsCategoryList

        var maxIdx = 0
        probability.forEachIndexed { index, category ->
            if (probability[maxIdx].score < category.score) {
                maxIdx = index
            }
        }

        binding.resView.text = labels[maxIdx]
        model.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            try {
                if (uri != null) {
                    bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    binding.imageView.setImageBitmap(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val photo = data.extras?.get("data") as Bitmap
            bitmap = photo
            binding.imageView.setImageBitmap(bitmap)
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, context: Context): Uri? {
        return try {
            val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openCamera()
            } else {
                Toast.makeText(requireActivity(), "Camera permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



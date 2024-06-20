package com.dicoding.dapurnusantara.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dapurnusantara.R
import com.dicoding.dapurnusantara.api.APIService
import com.dicoding.dapurnusantara.databinding.ActivityScanDetailBinding
import com.dicoding.dapurnusantara.dataclass.FoodDetail
import com.dicoding.dapurnusantara.dataclass.FoodDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScanDetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var detailresView: TextView
    private lateinit var foodCal: TextView
    private lateinit var foodRecipe: TextView
    private lateinit var foodDesc: TextView
    private lateinit var binding: ActivityScanDetailBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivityScanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the views using binding
        imageView = binding.detailImageView
        detailresView = binding.detailResView
        foodCal = binding.foodCal
        foodRecipe = binding.foodRecipe
        foodDesc = binding.foodDesc
        progressBar = binding.progressBar

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val label = intent.getStringExtra(EXTRA_PREDICTED_LABEL)

        binding.showMap.setOnClickListener {
            val placeName = getString(R.string.restaurant) + detailresView.text.toString()
            val gmmIntentUri = Uri.parse("geo:0,0?q=$placeName")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(this, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
            }
        }


        if (!imageUri.isNullOrEmpty()) {
            loadImageFromUri(Uri.parse(imageUri))
        }

        if (!label.isNullOrEmpty()) {
            detailresView.text = label
            // Fetch food details based on label
            fetchFoodDetails(label)
        }

        binding.saveButton.setOnClickListener {
            saveFoodData()
        }
    }

    private fun saveFoodData() {
        val foodName = detailresView.text.toString()
        val calories = foodCal.text.toString()
        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)

        val sharedPreferences = getSharedPreferences("food_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val foodList = sharedPreferences.getStringSet("food_list", mutableSetOf())
        foodList?.add("$imageUri|$foodName|$calories")

        editor.putStringSet("food_list", foodList)
        editor.apply()

        Toast.makeText(this, "Food data saved", Toast.LENGTH_SHORT).show()
    }

    private fun fetchFoodDetails(foodName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://darnus-api-v1-bhvchnngpq-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)
        val call = service.getFoodDetail(foodName)

        // Show the progress bar
        progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<FoodDetailResponse> {
            override fun onResponse(
                call: Call<FoodDetailResponse>,
                response: Response<FoodDetailResponse>
            ) {
                // Hide the progress bar
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val foodDetail = response.body()?.data
                    if (foodDetail != null) {
                        // Update UI with fetched data
                        updateUI(foodDetail)
                    } else {
                        Toast.makeText(this@ScanDetailActivity, "Food detail not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ScanDetailActivity, "Failed to fetch food detail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FoodDetailResponse>, t: Throwable) {
                // Hide the progress bar
                progressBar.visibility = View.GONE

                Toast.makeText(this@ScanDetailActivity, "Network error", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun updateUI(foodDetail: FoodDetail) {
        // Update UI elements
        foodCal.text = "${foodDetail.calories.joinToString(" - ")} Cal"
        foodRecipe.text = "Recipe:\n${foodDetail.recipe.joinToString("\n")}"
        foodDesc.text = foodDetail.description
    }

    private fun loadImageFromUri(uri: Uri) {
        imageView.setImageURI(uri)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PREDICTED_LABEL = "extra_predicted_label"
    }
}






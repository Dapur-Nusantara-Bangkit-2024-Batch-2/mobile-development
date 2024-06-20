package com.dicoding.dapurnusantara.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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

    private lateinit var binding: ActivityScanDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val label = intent.getStringExtra(EXTRA_PREDICTED_LABEL)

        binding.showMap.setOnClickListener {
            val placeName = getString(R.string.restaurant) + binding.detailResView.text.toString()
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
            binding.detailResView.text = label
            fetchFoodDetails(label)
        }

        binding.saveButton.setOnClickListener {
            saveFoodData()
        }
    }

    @SuppressLint("MutatingSharedPrefs")
    private fun saveFoodData() {
        val foodName = binding.detailResView.text.toString()
        val calories = binding.foodCal.text.toString()
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

        binding.progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<FoodDetailResponse> {
            override fun onResponse(
                call: Call<FoodDetailResponse>,
                response: Response<FoodDetailResponse>
            ) {
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val foodDetail = response.body()?.data
                    if (foodDetail != null) {
                        updateUI(foodDetail)
                    } else {
                        Toast.makeText(
                            this@ScanDetailActivity,
                            "Food detail not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ScanDetailActivity,
                        "Failed to fetch food detail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<FoodDetailResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE

                Toast.makeText(this@ScanDetailActivity, "Network error", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(foodDetail: FoodDetail) {
        binding.foodCal.text = "${foodDetail.calories.joinToString(" - ")} Cal"
        binding.foodRecipe.text = "Recipe:\n${foodDetail.recipe.joinToString("\n")}"
        binding.foodDesc.text = foodDetail.description
    }

    private fun loadImageFromUri(uri: Uri) {
        binding.detailImageView.setImageURI(uri)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PREDICTED_LABEL = "extra_predicted_label"
    }
}






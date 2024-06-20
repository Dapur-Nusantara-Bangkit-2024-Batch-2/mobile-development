package com.dicoding.dapurnusantara.dataclass

data class FoodDetailResponse(
    val error: Boolean,
    val data: FoodDetail
)

data class FoodDetail(
    val id: Int,
    val name: String,
    val calories: List<Int>,
    val recipe: List<String>,
    val description: String
)

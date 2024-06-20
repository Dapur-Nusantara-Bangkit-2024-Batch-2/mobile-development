package com.dicoding.dapurnusantara.api

import com.dicoding.dapurnusantara.dataclass.*
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @POST("/user/register")
    fun registUser(@Body requestRegister: RegisterDataAccount): Call<ResponseDetail>

    @POST("/user/login")
    fun loginUser(@Body requestLogin: LoginDataAccount): Call<ResponseLogin>

    @GET("/detail-food/{foodName}")
    fun getFoodDetail(@Path("foodName") foodName: String): Call<FoodDetailResponse>

}
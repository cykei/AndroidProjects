package com.cykei.birdview


import retrofit2.http.GET
import retrofit2.Call;
import retrofit2.http.Path

interface RetrofitInterface {
    @GET("products")
    fun requestAllProducts(): Call<ItemModel>

    @GET("2020-birdview/thumbnail/{img}")
    fun requestThumbnail(@Path("img") imgPath: Path): Call<ItemModel>
}
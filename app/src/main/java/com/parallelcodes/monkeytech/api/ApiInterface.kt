package com.parallelcodes.monkeytech.api

import com.parallelcodes.monkeytech.models.GiphySearchImagePoko

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {


    @GET("/v1/gifs/search?api_key=Zz7XnA0RZzJJetQAQv1e2c7ErivA9F5u")
    fun getData(@Query("q") key: String): Call<GiphySearchImagePoko>

    @GET("/v1/gifs/trending?api_key=Zz7XnA0RZzJJetQAQv1e2c7ErivA9F5u")
    fun getTitle(@Query("limit") key: Int?): Call<GiphySearchImagePoko>

}
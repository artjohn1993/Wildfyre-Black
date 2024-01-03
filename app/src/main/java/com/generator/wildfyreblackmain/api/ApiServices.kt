package com.generator.wildfyreblackmain.api

import com.generator.wildfyreblackmain.model.Wordpress
import io.reactivex.Observable
import retrofit2.http.*

interface ApiServices {

    @Headers("api-version: 1.0")
    @GET("")
    fun getlatestPost(@Url url : String) : Observable<List<Wordpress.Result>>

}
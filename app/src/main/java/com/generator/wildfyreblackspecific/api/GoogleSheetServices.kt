package com.generator.wildfyreblackspecific.api
import com.generator.wildfyreblackspecific.model.GoogleSheet
import io.reactivex.Observable
import retrofit2.http.*
interface GoogleSheetServices {

    @GET("")
    fun getUrl(@Url url : String) : Observable<GoogleSheet.Result>
}
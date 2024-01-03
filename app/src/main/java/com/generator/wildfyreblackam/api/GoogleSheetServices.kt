package com.generator.wildfyreblackam.api
import com.generator.wildfyreblackam.model.GoogleSheet
import io.reactivex.Observable
import retrofit2.http.*
interface GoogleSheetServices {

    @GET("")
    fun getUrl(@Url url : String) : Observable<GoogleSheet.Result>
}
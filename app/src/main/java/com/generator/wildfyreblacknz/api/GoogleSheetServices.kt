package com.generator.wildfyreblacknz.api
import com.generator.wildfyreblacknz.model.GoogleSheet
import io.reactivex.Observable
import retrofit2.http.*
interface GoogleSheetServices {

    @GET("")
    fun getUrl(@Url url : String) : Observable<GoogleSheet.Result>
}
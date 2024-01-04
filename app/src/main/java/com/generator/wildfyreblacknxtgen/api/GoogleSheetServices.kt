package com.generator.wildfyreblacknxtgen.api
import com.generator.wildfyreblacknxtgen.model.GoogleSheet
import io.reactivex.Observable
import retrofit2.http.*
interface GoogleSheetServices {

    @GET("")
    fun getUrl(@Url url : String) : Observable<GoogleSheet.Result>
}
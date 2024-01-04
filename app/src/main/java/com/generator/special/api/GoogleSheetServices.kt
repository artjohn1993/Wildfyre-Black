package com.generator.special.api
import com.generator.special.model.GoogleSheet
import io.reactivex.Observable
import retrofit2.http.*
interface GoogleSheetServices {

    @GET("")
    fun getUrl(@Url url : String) : Observable<GoogleSheet.Result>
}
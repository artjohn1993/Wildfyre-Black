package com.generator.pageone.api
import com.generator.pageone.model.GoogleSheet
import io.reactivex.Observable
import retrofit2.http.*
interface GoogleSheetServices {

    @GET("")
    fun getUrl(@Url url : String) : Observable<GoogleSheet.Result>

    @GET("")
    fun getSheet(@Url url : String) : Observable<GoogleSheet.SheetResult>
}
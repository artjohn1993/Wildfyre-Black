package com.generator.wildfyre.model

object GoogleSheet {

    data class Result (
        var range : String,
        var majorDimension : String,
        var values : MutableList<MutableList<String>>
    )

    data class SheetResult (
        var sheets : List<Sheets>
    )

    data class Sheets (
        var properties : Properties
    )

    data class Properties(
        var title : String,
        var index : Int
    )

    data class Settings(
        var sheet_name : String
    )
}
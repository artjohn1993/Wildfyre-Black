package com.generator.wildfyreblacknz.model

object GoogleSheet {

    data class Result (
        var range : String,
        var majorDimension : String,
        var values : MutableList<MutableList<String>>
        )
}
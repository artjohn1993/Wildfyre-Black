package com.generator.wildfyreblackam.model

object Wordpress {

    data class Result (
        var group : String?,
        var link : String,
        var date : String,
        var title : Title
    )

    data class Title(
        var rendered : String
    )
}
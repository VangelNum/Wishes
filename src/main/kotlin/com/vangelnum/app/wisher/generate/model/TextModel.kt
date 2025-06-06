package com.vangelnum.app.wisher.generate.model

data class TextModel(
    val name: String,
    val type: String,
    val censored: Boolean,
    val description: String,
    val baseModel: Boolean,
    val vision: Boolean? = null
)
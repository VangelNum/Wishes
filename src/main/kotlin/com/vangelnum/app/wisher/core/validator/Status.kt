package com.vangelnum.app.wisher.core.validator

data class Status(
    val isSuccess: Boolean,
    val message: String? = null
)
package com.vangelnum.app.wisher.core.utils

import org.springframework.security.core.context.SecurityContextHolder

fun getCurrentUserEmail(): String {
    val authentication = SecurityContextHolder.getContext().authentication
    return authentication.name
}

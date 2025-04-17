package com.vangelnum.app.wisher.translate.service

interface TranslationService {
    fun translate(text: String, langpair: String): String?
}
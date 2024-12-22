package com.vangelnum.app.wisher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WisherApplication

fun main(args: Array<String>) {
    runApplication<WisherApplication>(*args)
}
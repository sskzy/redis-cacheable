package com.example.kgrediscacheable

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KgRedisCacheableApplication

fun main(args: Array<String>) {
    runApplication<KgRedisCacheableApplication>(*args)
}

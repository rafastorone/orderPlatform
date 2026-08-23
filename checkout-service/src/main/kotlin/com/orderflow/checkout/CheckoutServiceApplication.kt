package com.orderflow.checkout

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CheckoutServiceApplication

fun main(args: Array<String>) {
    runApplication<CheckoutServiceApplication>(*args)
}

package com.ecommerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class EcommerceProjectApplication

fun main(args: Array<String>) {
    runApplication<EcommerceProjectApplication>(*args)
}

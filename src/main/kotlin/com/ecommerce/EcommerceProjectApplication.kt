package com.ecommerce

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class EcommerceProejctApplication

fun main(args: Array<String>) {
    runApplication<EcommerceProejctApplication>(*args)
}

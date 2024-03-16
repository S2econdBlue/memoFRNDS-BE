package xyz.hae.haeback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HaeBackApplication

fun main(args: Array<String>) {
    runApplication<HaeBackApplication>(*args)
}

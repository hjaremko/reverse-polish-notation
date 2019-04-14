package pl.jaremko.rpn

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    while (true) {
        val rawInput = scanner.nextLine()

        if (rawInput.toLowerCase() == "quit") {
            break
        }

        try {
            println(rawInput.toExpression().getConverted())
        }
        catch (e: Exception) {
            println(e.message)
        }
    }
}
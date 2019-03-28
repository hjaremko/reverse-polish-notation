package pl.jaremko.rpn

import java.lang.Exception
import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    while ( true ) {
        val rawExpression = scanner.nextLine()

        if (rawExpression.toLowerCase() == "quit")
            break

        try {
            val expression = ExpressionFactory.createFrom(rawExpression)
            expression.stripInvalid()
            expression.checkValidity()
            println(expression.getConverted())
        }
        catch (e: Exception) {
            println(e.message)
        }
    }
}
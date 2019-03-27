package pl.jaremko.rpn

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    while ( true ) {
        val rawExpression = scanner.nextLine()

        if (rawExpression.toLowerCase() == "quit")
            break

        val expression = ExpressionFactory.createFrom(rawExpression)
        expression.stripInvalid()
        println(expression.getConverted())
    }
}
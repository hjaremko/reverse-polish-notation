package pl.jaremko.rpn

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    var expressionCount = scanner.nextInt()
    scanner.nextLine()

    while (expressionCount-- > 0) {
        val expression = ExpressionFactory.createFrom(scanner.nextLine())
        expression.stripInvalid()
        println(expression.getConverted())
    }
}
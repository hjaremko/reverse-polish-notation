package pl.jaremko.rpn

import java.lang.IllegalArgumentException

fun String.getType(): String = this.substring( 0, 3 ).toUpperCase()
fun String.getExpression(): String = this.substring( 5 )

object ExpressionFactory {
    fun createFrom( rawExpression: String ) : Expression {
        return when (rawExpression.getType()) {
            "RPN" -> RpnExpression(rawExpression.getExpression())
            "INF" -> InfExpression(rawExpression.getExpression())
            else -> throw IllegalArgumentException("Invalid expression type ${rawExpression.getType()}!")
        }
    }
}
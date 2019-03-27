package pl.jaremko.rpn

fun String.getType(): String = this.substring( 0, 3 )
fun String.getExpression(): String = this.substring( 5 )

class InvalidExpressionType(message: String?) : Exception(message)

object ExpressionFactory {
    fun createFrom( rawExpression: String ) : Expression {
        return when (rawExpression.getType()) {
            "RPN" -> RpnExpression(rawExpression.getExpression())
            "INF" -> InfExpression(rawExpression.getExpression())
            else -> throw InvalidExpressionType("Invalid expression type ${rawExpression.getType()}!")
        }
    }
}
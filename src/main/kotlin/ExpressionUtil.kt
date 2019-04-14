package pl.jaremko.rpn

fun String.getType(): String = this.substring(0, 3).toUpperCase()
fun String.getExpression(): String = this.substring(5)

fun String.toExpression(): Expression {
    val expr = when (this.getType()) {
        "RPN" -> RpnExpression(this.getExpression())
        "INF" -> InfExpression(this.getExpression())
        else -> throw IllegalArgumentException("Invalid expression type ${this.getType()}!")
    }

    expr.stripInvalid()
    expr.checkValidity()
    return expr
}

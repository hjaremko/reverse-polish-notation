package pl.jaremko.rpn

import pl.jaremko.rpn.Token.Type.*
import pl.jaremko.rpn.Token.Associativity.*
import java.lang.IllegalArgumentException
import java.util.*

class RpnExpression(private var expression: String) : Expression {

    override fun toString(): String = "RPN: $expression"

    override fun stripInvalid() {
        expression = expression.replace("[^a-z=<>+\\-/%^~*]".toRegex(), "")
    }

    override fun checkValidity() {
        val operators = expression.count { it.tokenize().type == Binary }
        val operands  = expression.count { it.tokenize().type == Operand }

        if (operators != (operands - 1))
            throw IllegalArgumentException("RPN expression '$expression' is not calculable!")
    }

    override fun getConverted(): Expression {
        val deque = ArrayDeque<Token>()

        for (t in expression) {
            val token: Token = t.tokenize()
            val bracedToken = when (token.type) {
                Operand -> token
                Binary -> {
                    val rhs = deque.pop()
                    val lhs = deque.pop()

                    val out = when (token.associativity) {
                        Right -> addBraces(lhs, rhs, token, { a, b -> a <= b }, { a, b -> a < b  } )
                        Left  -> addBraces(lhs, rhs, token, { a, b -> a <  b }, { a, b -> a <= b } )
                        None -> ""
                    }

                    Token(out, token.priority)
                }
                Unary -> Token(addBracesUnary(deque.pop(), token), token.priority)
                else -> null
            }

            deque.push(bracedToken)
        }

        return InfExpression(deque.peek().toString())
    }

    private inline fun addBraces(lhs: Token, rhs: Token, operator: Token, comp: (Int, Int) -> Boolean, comp2: (Int, Int) -> Boolean): String =
        (if (comp(lhs.priority, operator.priority))
            "($lhs)"
        else
            lhs.toString()) + operator + if (comp2(rhs.priority, operator.priority))
                                             "($rhs)"
                                         else rhs

    private fun addBracesUnary(rhs: Token, operator: Token): String =
        if (rhs.priority < operator.priority)
            "~($rhs)"
        else
            "~$rhs"
}
package pl.jaremko.rpn

import pl.jaremko.rpn.Token.Type.*
import pl.jaremko.rpn.Token.Associativity.*
import java.util.*

class RpnExpression(private var expression: String) : Expression {

    override fun toString(): String = "RPN: $expression"

    override fun stripInvalid() {
        expression = expression.replace("[^a-z=<>+\\-/%^~*]".toRegex(), "")
    }

    override fun isValid(): Boolean {
        var operators = 0
        var operands = 0

        for (t in expression) {
            val token = t.tokenize()

            when (token.type) {
                Operand -> operands++
                Binary -> operators++
                else -> Unit
            }
        }

        return operators == (operands - 1)
    }

    override fun getConverted(): Expression {
        if (isValid()) {
            val deque = ArrayDeque<Token>()

            for (t in expression) {
                val token: Token = t.tokenize()
                val bracedToken = when (token.type) {
                    Operand -> token

                    Binary -> {
                        val rhs = deque.pop()
                        val lhs = deque.pop()

                        val out = when (token.associativity) {
                            Right -> addBracesRight(lhs, rhs, token)
                            Left -> addBracesLeft(lhs, rhs, token)
                            None -> ""
                        }

                        Token(out, token.priority)
                    }

                    Unary -> {
                        Token(addBracesUnary(deque.pop(), token), token.priority)
                    }

                    else -> null
                }

                deque.push(bracedToken)
            }

            return InfExpression(deque.peek().toString())
        }

        return InfExpression("error")
    }

    private fun addBracesRight(lhs: Token, rhs: Token, operator: Token): String =
        (if (lhs.priority <= operator.priority) "($lhs)" else lhs.toString()) + operator + if (rhs.priority < operator.priority) "($rhs)" else rhs

    private fun addBracesLeft(lhs: Token, rhs: Token, operator: Token): String =
        (if (lhs.priority < operator.priority) "($lhs)" else lhs.toString()) + operator + if (rhs.priority <= operator.priority) "($rhs)" else rhs

    private fun addBracesUnary(rhs: Token, operator: Token): String =
        if (rhs.priority < operator.priority) "~($rhs)" else "$operator$rhs"
}
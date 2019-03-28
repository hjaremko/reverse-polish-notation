package pl.jaremko.rpn

import pl.jaremko.rpn.Token.Type.*
import pl.jaremko.rpn.Token.Associativity.*
import java.lang.IllegalArgumentException
import java.util.*

private fun ArrayDeque<Token>.popAll(): String {
    var out = ""

    while (!this.isEmpty()) {
        val token: Token = this.pop()

        if (token.type == OpeningBrace) {
            break
        }

        out += token
    }

    return out
}

private inline fun ArrayDeque<Token>.popWhile(condition: (Int) -> Boolean): String {
    var out = ""

    while (!this.isEmpty() && condition(this.peek().priority) && this.peek().type != OpeningBrace) {
        out += this.pop()
    }

    return out
}

class InfExpression(private var expression: String) : Expression {

    override fun toString(): String = "INF: $expression"

    override fun stripInvalid() {
        expression = expression.replace("[^a-z=<>+\\-/%^~*()]".toRegex(), "")
    }

    override fun checkValidity() {
        var state = 0
        var leftBraces = 0
        var rightBraces = 0
        val error = IllegalArgumentException("'$expression' is not valid infix expression!")

        for (t in expression) {
            val token = t.tokenize()

            when (state) {
                0 -> state = when (token.type) {
                    OpeningBrace -> {
                        leftBraces++
                        0
                    }
                    Operand -> 1
                    Unary -> 2
                    else -> throw error
                }
                1 -> state = when (token.type) {
                    ClosingBrace -> {
                        if (leftBraces == 0)
                            throw error

                        rightBraces++
                        1
                    }
                    Binary -> 0
                    else -> throw error
                }
                2 -> state = when (token.type) {
                    OpeningBrace -> {
                        leftBraces++
                        0
                    }
                    Operand -> 1
                    Unary -> 2
                    else -> throw error
                }
            }
        }

        if (state != 1 || leftBraces != rightBraces) {
            throw error
        }
    }

    override fun getConverted(): Expression {
        val deque = ArrayDeque<Token>()
        var out = ""

        for (t in expression) {
            val token: Token = t.tokenize()

            when (token.type) {
                Operand -> out += token
                ClosingBrace -> out += deque.popAll()
                else -> {
                    out += when (token.associativity) {
                        Left -> deque.popWhile { a -> a >= token.priority }
                        Right -> deque.popWhile { a -> a > token.priority }
                        None -> ""
                    }
                    deque.push(token)
                }
            }
        }

        while (!deque.isEmpty())
            out += deque.pop()

        return RpnExpression(out)
    }
}

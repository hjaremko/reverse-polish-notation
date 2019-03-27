package pl.jaremko.rpn

import pl.jaremko.rpn.Token.Type.*
import pl.jaremko.rpn.Token.Associativity.*
import java.util.*

private fun ArrayDeque<Token>.popAll(): String {
    var out = ""
    var token: Token

    while (!this.isEmpty()) {
        token = this.pop()
        if (token.type == OpeningBrace) break
        out += token
    }

    return out
}

private fun ArrayDeque<Token>.popGreaterEqual(token: Token): String {
    var out = ""

    while (!this.isEmpty() &&
        this.peek().priority >= token.priority &&
        this.peek().type != OpeningBrace
    ) {
        out += this.pop()
    }

    this.push(token)
    return out
}

private fun ArrayDeque<Token>.popGreater(token: Token): String {
    var out = ""

    while (!this.isEmpty() &&
        this.peek().priority > token.priority &&
        this.peek().type != OpeningBrace
    ) {
        out += this.pop()
    }

    this.push(token)
    return out
}

class InfExpression(private var expression: String) : Expression {

    override fun toString(): String = "INF: $expression"

    override fun stripInvalid() {
        expression = expression.replace("[^a-z=<>+\\-/%^~*()]".toRegex(), "")
    }

    override fun isValid(): Boolean {
        var state = 0
        var leftBraces = 0
        var rightBraces = 0

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
                    else -> return false
                }
                1 -> state = when (token.type) {
                    ClosingBrace -> {
                        if (leftBraces == 0)
                            return false

                        rightBraces++
                        1
                    }
                    Binary -> 0
                    else -> return false
                }
                2 -> state = when (token.type) {
                    OpeningBrace -> {
                        leftBraces++
                        0
                    }
                    Operand -> 1
                    Unary -> 2
                    else -> return false
                }
            }
        }

        return state == 1 && leftBraces == rightBraces
    }

    override fun getConverted(): Expression {
        if (isValid()) {
            val deque = ArrayDeque<Token>()
            var out = ""

            for (t in expression) {
                val token: Token = t.tokenize()

                when (token.type) {
                    Operand -> out += token
                    OpeningBrace -> deque.push(token)
                    ClosingBrace -> out += deque.popAll()
                    else -> when (token.associativity) {
                        Left -> out += deque.popGreaterEqual(token)
                        Right -> out += deque.popGreater(token)
                        None -> Unit
                    }
                }
            }

            while (!deque.isEmpty())
                out += deque.pop()

            return RpnExpression(out)
        }

        return RpnExpression("error")
    }
}

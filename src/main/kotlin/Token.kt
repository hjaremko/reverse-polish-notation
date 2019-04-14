package pl.jaremko.rpn

data class Token(
    val value: String,
    val priority: Int = 6,
    val type: Type = Type.Operand,
    val associativity: Associativity = Associativity.None
) {

    enum class Type {
        Operand,
        OpeningBrace,
        ClosingBrace,
        Binary,
        Unary
    }

    enum class Associativity {
        Left,
        Right,
        None
    }

    override fun toString() = value

    fun addBraces(condition: Boolean) = if (condition) "($this)" else this.toString()
}

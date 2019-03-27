package pl.jaremko.rpn

interface Expression {
    fun stripInvalid()
    fun getConverted(): Expression
    fun isValid(): Boolean
}


package pl.jaremko.rpn

fun Char.tokenize() = when (this) {
    '=' -> Token(Character.toString(this), 0, Token.Type.Binary, Token.Associativity.Right)
    '>' -> Token(Character.toString(this), 1, Token.Type.Binary, Token.Associativity.Left)
    '<' -> Token(Character.toString(this), 1, Token.Type.Binary, Token.Associativity.Left)
    '+' -> Token(Character.toString(this), 2, Token.Type.Binary, Token.Associativity.Left)
    '-' -> Token(Character.toString(this), 2, Token.Type.Binary, Token.Associativity.Left)
    '*' -> Token(Character.toString(this), 3, Token.Type.Binary, Token.Associativity.Left)
    '/' -> Token(Character.toString(this), 3, Token.Type.Binary, Token.Associativity.Left)
    '%' -> Token(Character.toString(this), 3, Token.Type.Binary, Token.Associativity.Left)
    '^' -> Token(Character.toString(this), 4, Token.Type.Binary, Token.Associativity.Right)
    '~' -> Token(Character.toString(this), 5, Token.Type.Unary, Token.Associativity.Right)
    '(' -> Token(Character.toString(this), 6, Token.Type.OpeningBrace, Token.Associativity.None)
    ')' -> Token(Character.toString(this), 6, Token.Type.ClosingBrace, Token.Associativity.None)
    else -> Token(Character.toString(this), 6, Token.Type.Operand, Token.Associativity.None)
}

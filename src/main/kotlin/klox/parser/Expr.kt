package klox.parser

import klox.tokenizer.Token

interface Expr

data class BinaryExpr(
    val first: Expr,
    val operator: Token,
    val second: Expr,
) : Expr {
    override fun toString() = "(${operator.type} $first $second)"
}

data class UnaryExpr(
    val operator: Token,
    val expr: Expr,
) : Expr {
    override fun toString() = "(${operator.type} $expr)"
}

data class Literal(
    val value: Any?,
) : Expr {
    override fun toString() = "$value"
}

data class Grouping(
    val expr: Expr,
) : Expr {
    override fun toString() = "($expr)"
}

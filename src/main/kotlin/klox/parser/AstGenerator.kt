package klox.parser 

fun generate(expr: Expr): Any {
    return when (expr) {
        is BinaryExpr,
        is UnaryExpr -> generate(expr)
        else -> throw IllegalArgumentException()
    }
}

fun generate(expr: BinaryExpr) {
    // ast node
    // operator generate(expr.first) generate(expr.second)
}

fun generate(expr: UnaryExpr) {

}

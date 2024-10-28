package klox.parser

import klox.tokenizer.Token

interface Expr

data class BinaryExpr(
  val operator: Token,
  val first: Expr,
  val second: Expr,
) : Expr {
  override fun toString() = "($operator $first $second)"
}

data class UnaryExpr(
  val operator: Token,
  val expr: Expr,
) : Expr {
   override fun toString() = "($operator $expr)"  
}

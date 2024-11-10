package klox.interpreter

import klox.parser.Expr
import klox.parser.BinaryExpr
import klox.parser.UnaryExpr
import klox.parser.Literal
import klox.parser.Grouping
import klox.tokenizer.TokenType.*

fun interpret(expr: Expr): Any? {
    return when(expr) {
      is BinaryExpr -> interpret(expr)
      is UnaryExpr -> interpret(expr)
      is Literal -> interpret(expr)
      is Grouping -> interpret(expr)
      else -> throw RuntimeException("Expression ${expr} not supported")
    }
}

fun interpret(expr: BinaryExpr): Any? {
   val left = interpret(expr.first)
   val right = interpret(expr.second)

   return when(expr.operator.type) {
     PLUS ->
       when {
          left is Double && right is Double -> left + right
          left is String && right is String -> left + right
          else -> throw RuntimeException("Unsupported operation [${expr.operator.lineNumber}]. Can only add Numbers and Strings")
       }
     MINUS -> (left as Double) - (right as Double)
     STAR -> (left as Double) * (right as Double)
     SLASH -> (left as Double) / (right as Double)
     LESS_THAN -> (left as Double) < (right as Double)
     LESS_THAN_EQUAL -> (left as Double) <= (right as Double)
     GREATER_THAN -> (left as Double) > (right as Double)
     GREATER_THAN_EQUAL -> (left as Double) >= (right as Double)
     EQUAL_EQUAL -> left == right
     NOT_EQUAL -> left != right
     else -> throw RuntimeException("${expr.operator.type} is not a supported binary operator")
   }
}

fun interpret(expr: UnaryExpr): Any? {
   val value = interpret(expr.expr) 

   return when(expr.operator.type) {
     NOT -> !isTruthy(value)
     MINUS -> -(value as Double)
     else -> throw RuntimeException("Unary expression ${expr.operator.type} is not supported")
   }
}

fun interpret(expr: Literal): Any? = expr.value
fun interpret(expr: Grouping): Any? = interpret(expr.expr)

private fun isTruthy(value: Any?) = value != null && (value as? Boolean)?.let { it } ?: true

fun Any?.stringify(): String {
    if (this == null) return "nil"
    if (this is Double) {
       return this.toString().dropLast(2)
    }

    return this.toString()
}


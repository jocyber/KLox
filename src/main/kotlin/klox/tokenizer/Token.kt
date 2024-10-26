package klox.tokenizer

data class Token(
  val type: TokenType,
  val lineNumber: Int,
  val lexeme: String?,
  val value: Any?,
)

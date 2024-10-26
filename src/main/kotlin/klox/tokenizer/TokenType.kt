package klox.tokenizer

enum class TokenType {
  /** Data types */
  NUMBER,
  STRING,

  /** Operators and symbols */
  SEMICOLON,
  LEFT_PAREN,
  RIGHT_PAREN,
  DOT,
  COMMA,
  PLUS,
  MINUS,
  STAR,
  SLASH,
  ASSIGNMENT,
  WHITESPACE,
  COMMENT,
  LEFT_CURLY,
  RIGHT_CURLY,
  LESS_THAN,
  LESS_THAN_EQUAL,
  GREATER_THAN,
  GREATER_THAN_EQUAL,
  EQUAL_EQUAL,
  NOT_EQUAL,
  NOT,
  IDENTIFIER,

  /** Keywords. */
  VAR,
  IF,
  ELSE,
  WHILE,
  CLASS,
  PRINT,
  FUN,
  RETURN,
  FOR,
  AND,
  OR,
  SUPER, 
  NIL, 
  THIS,
  TRUE,
  FALSE,

  EOF,
  ERROR,
}

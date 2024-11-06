package klox.tokenizer

import klox.tokenizer.TokenType.*

/** Scanner with a lookahead of 1. */
class Scanner(private val source: String) {
    private var cursor = 0
    private var current = 0
    private var line = 1
    private val tokens = mutableListOf<Token>()

    fun scan(): List<Token> {
        while (!atEnd()) {
            cursor = current
            parseToken()
        }

        return tokens.apply { addToken(EOF) }
    }

    private fun parseToken() {
        val currentChar = advance()

        when (currentChar) {
            ';' -> addToken(SEMICOLON)
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '.' -> addToken(DOT)
            ',' -> addToken(COMMA)
            '+' -> addToken(PLUS)
            '-' -> addToken(MINUS)
            '*' -> addToken(STAR)
            '{' -> addToken(LEFT_CURLY)
            '}' -> addToken(RIGHT_CURLY)
            '!' -> if (match('=')) addToken(NOT_EQUAL) else addToken(NOT)
            '=' -> if (match('=')) addToken(EQUAL_EQUAL) else addToken(ASSIGNMENT)
            '<' -> if (match('=')) addToken(LESS_THAN_EQUAL) else addToken(LESS_THAN)
            '>' -> if (match('=')) addToken(GREATER_THAN_EQUAL) else addToken(GREATER_THAN)
            '/' -> {
                if (match('/')) {
                    while (!atEnd() && peek() != '\n') advance()
                    addToken(COMMENT)
                } else {
                    addToken(SLASH)
                }
            }
            '"' -> handleString()
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            '0' -> handleNumber()
            ' ' -> addToken(WHITESPACE)
            '\n' -> {
                addToken(WHITESPACE)
                line++
            }
            else -> {
                if (currentChar.isAlpha()) {
                    handleIdentifier()
                } else {
                    addToken(ERROR, "Invalid character $currentChar")
                }
            }
        }
    }

    private fun addErrorToken(message: String) = addToken(ERROR, message)

    private fun addToken(type: TokenType) = addToken(type, "")

    private fun addToken(type: TokenType, lexeme: String) =
        tokens.add(Token(type, line, lexeme, null))

    private fun match(expectedChar: Char): Boolean {
        if (atEnd()) return false.also { addErrorToken("$line: Unexpected end of input") }

        return (source[current] == expectedChar).also { if (it) advance() }
    }

    private fun atEnd() = current >= source.length

    /** Consumes the next character and increments the lookahead. */
    private fun advance() = source[current++]

    private fun peek(): Char? = if (atEnd()) null else source[current]

    /** source parsing helpers */
    private fun handleString() {
        val startingLine = line

        while (peek() != '"') {
            if (atEnd()) {
                addErrorToken("$line: Unterminated string")
                return
            }
            if (peek() == '\n') line++

            advance()
        }
        advance()

        val stringLiteral = source.substring(cursor + 1, current - 1)
        tokens.add(Token(STRING, startingLine, "\"$stringLiteral\"", stringLiteral))
    }

    private fun handleNumber() {
        while (peek()?.isDigit() ?: false) advance()

        if (peek() == '.') {
            advance()

            when {
                peek() == null -> {
                    addErrorToken("$line: Unexpected end of input")
                    return
                }
                !peek()!!.isDigit() -> {
                    addErrorToken("$line: Unterminated number")
                    return
                }
                else -> while (peek()!!.isDigit()) advance()
            }
        }

        val number = source.substring(cursor, current)
        tokens.add(Token(NUMBER, line, number, number.toDouble()))
    }

    private fun handleIdentifier() {
        while (peek()?.isAlpha() ?: false) advance()

        val identifer = source.substring(cursor, current)
        val keywordType = KEYWORDS[identifer]

        if (keywordType == null) {
            addToken(IDENTIFIER, identifer)
        } else {
            addToken(keywordType)
        }
    }

    companion object {
        private val KEYWORDS =
            mapOf(
                "var" to VAR,
                "if" to IF,
                "else" to ELSE,
                "while" to WHILE,
                "class" to CLASS,
                "print" to PRINT,
                "fun" to FUN,
                "return" to RETURN,
                "for" to FOR,
                "and" to AND,
                "or" to OR,
                "super" to SUPER,
                "nil" to NIL,
                "this" to THIS,
                "true" to TRUE,
                "false" to FALSE,
            )

        private fun Char.isAlpha() =
            this == '_' || (this >= 'a' && this <= 'z') || (this >= 'A' && this <= 'Z')
    }
}

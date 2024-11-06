package klox.parser

import klox.tokenizer.Token
import klox.tokenizer.TokenType
import klox.tokenizer.TokenType.*

class Parser(tokens: List<Token>) {
    private var current = 0
    private val tokens = tokens.filter { it.type !in setOf(COMMENT, WHITESPACE) }
    private lateinit var matchedToken: Token

    fun parse(): Expr {
        return try {
            expression()
        } catch (e: ParseError) {
            throw e
        }
    }

    fun expression() = equality()

    /** equality -> comparison ((!= | ==) comparison)* */
    fun equality(): Expr {
        var expr = comparison()

        while (match(EQUALITY_TYPES)) {
            val operator = matchedToken
            val rightExpr = comparison()

            expr = BinaryExpr(expr, operator, rightExpr)
        }

        return expr
    }

    /** comparison -> term ((> | >= | < | <=) term)* */
    fun comparison(): Expr {
        var expr = term()

        while (match(COMPARISON_TYPES)) {
            val operator = matchedToken
            val rightExpr = term()

            expr = BinaryExpr(expr, operator, rightExpr)
        }

        return expr
    }

    /** term -> factor ((+ | -) factor)* */
    fun term(): Expr {
        var expr = factor()

        while (match(TERM_TYPES)) {
            val operator = matchedToken
            val rightExpr = factor()

            expr = BinaryExpr(expr, operator, rightExpr)
        }

        return expr
    }

    /** factor -> unary ((/ | *) unary)* */
    fun factor(): Expr {
        var expr = unary()

        while (match(FACTOR_TYPES)) {
            val operator = matchedToken
            val rightExpr = unary()

            expr = BinaryExpr(expr, operator, rightExpr)
        }

        return expr
    }

    /** unary -> primary | (! | -) unary */
    fun unary(): Expr {
        return if (match(UNARY_TYPES)) {
            val operator = matchedToken
            val unaryExpr = unary()

            UnaryExpr(operator, unaryExpr)
        } else {
            primary()
        }
    }

    /** primary -> String | Number | true | false | nil | "(" expression ")" */
    fun primary(): Expr {
        if (match(setOf(TRUE))) return Literal(true)
        if (match(setOf(FALSE))) return Literal(false)
        if (match(setOf(NIL))) return Literal(null)

        if (match(setOf(STRING, NUMBER))) return Literal(matchedToken.value)

        if (match(setOf(LEFT_PAREN))) {
            val expression = expression()

            if (match(setOf(RIGHT_PAREN))) {
                return Grouping(expression)
            } else {
                throw ParseError("Expected ')' at end of expression")
            }
        } else {
            throw ParseError("primary expression ${peek()} not supported")
        }
    }

    private fun match(tokenTypes: Set<TokenType>): Boolean {
        val currentToken = peek()

        return tokenTypes.contains(currentToken.type).also {
            if (it) {
                matchedToken = currentToken
                advance()
            }
        }
    }

    private fun isEnd() = peek().type == EOF

    private fun peek() = tokens[current]

    private fun advance() = current++

    companion object {
        val EQUALITY_TYPES = setOf(NOT_EQUAL, EQUAL_EQUAL)
        val COMPARISON_TYPES = setOf(LESS_THAN, GREATER_THAN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL)
        val TERM_TYPES = setOf(PLUS, MINUS)
        val FACTOR_TYPES = setOf(SLASH, STAR)
        val UNARY_TYPES = setOf(NOT, MINUS)
    }
}

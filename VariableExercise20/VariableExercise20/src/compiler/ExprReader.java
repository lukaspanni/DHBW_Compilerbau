package compiler;

public class ExprReader extends ExprReaderIntf {

	public ExprReader(SymbolTable symbolTable, LexerIntf lexer) throws Exception {
		super(symbolTable, lexer);
	}

	@Override
	public int getAtomicExpr() throws Exception {
		int number = 0;
		Token token = this.m_lexer.lookAheadToken();
		if (token.m_type == Token.Type.INTEGER) {
			this.m_lexer.advance();
			number = token.m_intValue;
		} else if (token.m_type == Token.Type.LPAREN) {
			this.m_lexer.advance();
			number = getExpr();
			this.m_lexer.expect(Token.Type.RPAREN);
		} else if (token.m_type == Token.Type.IDENT) {
			this.m_lexer.advance();
			Symbol var = this.m_symbolTable.getSymbol(token.m_stringValue);
			number = var.m_number;
		} else
			throw new ParserException("Unexpected Token: ", token.toString(), this.m_lexer.getCurrentLocationMsg(), "numerical expression");
		return number;
	}

	@Override
	public int getUnaryExpr() throws Exception {
		boolean neg = false;
		Token token = this.m_lexer.lookAheadToken();
		while (token.m_type == Token.Type.MINUS) {
			this.m_lexer.advance();
			neg = !neg;
			token = this.m_lexer.lookAheadToken();
		}
		int number = getAtomicExpr();
		return neg ? -number : number;
	}

	@Override
	public int getProduct() throws Exception {
		int number = getUnaryExpr();
		Token token = this.m_lexer.lookAheadToken();
		while (token.m_type == Token.Type.MUL || token.m_type == Token.Type.DIV) {
			this.m_lexer.advance();
			if (token.m_type == Token.Type.MUL) {
				number *= getUnaryExpr();
			} else {
				number /= getUnaryExpr();
			}
			token = this.m_lexer.lookAheadToken();
		}
		return number;
	}


	@Override
	public int getExpr() throws Exception {
		int number = getProduct();
		Token token = this.m_lexer.lookAheadToken();
		while (token.m_type == Token.Type.PLUS || token.m_type == Token.Type.MINUS) {
			this.m_lexer.advance();
			if (token.m_type == Token.Type.PLUS) {
				number += getProduct();
			} else {
				number -= getProduct();
			}
			token = this.m_lexer.lookAheadToken();
		}
		return number;
	}

}

package compiler;

public class ExprReader extends ExprReaderIntf {

	public ExprReader(SymbolTable symbolTable, LexerIntf lexer, CompileEnvIntf compileEnv) throws Exception {
		super(symbolTable, lexer, compileEnv);
	}
	
	public void getAtomicExpr() throws Exception {
		Token token = m_lexer.lookAheadToken(); 
		if (token.m_type == Token.Type.INTEGER) {
			m_lexer.advance();
			// create instruction
		} else if (token.m_type == Token.Type.LPAREN) {
			m_lexer.advance();
			getExpr();
			m_lexer.expect(Token.Type.RPAREN);
		} else if (token.m_type == Token.Type.IDENT) {
			m_lexer.advance();
			// create instruction
		} else {
			throw new ParserException("Unexpected Token: ", token.toString(), m_lexer.getCurrentLocationMsg(), "numerical expression");
		}
	}
	
	public void getUnaryExpr() throws Exception {
		Token token = m_lexer.lookAheadToken(); 
		getAtomicExpr();
		while (token.m_type == Token.Type.MINUS) {
			m_lexer.advance();
			// create instruction
			token = m_lexer.lookAheadToken();
		}
	}

	public void getProduct() throws Exception {
		getUnaryExpr();
		Token token = m_lexer.lookAheadToken(); 
		while (token.m_type == Token.Type.MUL || token.m_type == Token.Type.DIV) {
			m_lexer.advance();
			if (token.m_type == Token.Type.MUL) {
				getUnaryExpr();
				// create instruction
			} else {
				getUnaryExpr();
				// create instruction
			}
			token = m_lexer.lookAheadToken(); 
		}
	}

	public void getExpr() throws Exception {
		getProduct();
		Token token = m_lexer.lookAheadToken(); 
		while (token.m_type == Token.Type.PLUS || token.m_type == Token.Type.MINUS) {
			m_lexer.advance();
			if (token.m_type == Token.Type.PLUS) {
				getProduct();
				// create instruction
			} else {
				getProduct();
				// create instruction
			}
			token = m_lexer.lookAheadToken(); 
		}
	}

}

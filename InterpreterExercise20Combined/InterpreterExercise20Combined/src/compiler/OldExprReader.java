package compiler;

public class OldExprReader extends ExprReaderIntf {

	public OldExprReader(SymbolTable symbolTable, LexerIntf lexer, CompileEnvIntf compileEnv) throws Exception {
		super(symbolTable, lexer, compileEnv);
	}
	
	public void getAtomicExpr() throws Exception {
		Token token = m_lexer.lookAheadToken(); 
		if (token.m_type == Token.Type.INTEGER) {
			m_lexer.advance();
			// create instruction
			InstrIntf numberInstr = new Instr.PushNumberInstr(token.m_intValue);
			m_compileEnv.addInstr(numberInstr);
		} else if (token.m_type == Token.Type.LPAREN) {
			m_lexer.advance();
			getExpr();
			m_lexer.expect(Token.Type.RPAREN);
		} else if (token.m_type == Token.Type.IDENT) {
			m_lexer.advance();
			// create instruction
			InstrIntf variableInstr = new Instr.VariableInstr(token.m_stringValue);
			m_compileEnv.addInstr(variableInstr);
		} else {
			throw new ParserException("Unexpected Token: ", token.toString(), m_lexer.getCurrentLocationMsg(), "numerical expression");
		}
	}
	
	public void getUnaryExpr() throws Exception {
		Token token = m_lexer.lookAheadToken();
		int sign = 0;
		while (token.m_type == Token.Type.MINUS) {
			m_lexer.advance();
			sign += 1;
			token = m_lexer.lookAheadToken();
		}
		getAtomicExpr();
		// create instruction
		while (sign > 0) {
		  InstrIntf unaryMinusExpr = new Instr.UnaryMinusInstr();
		  m_compileEnv.addInstr(unaryMinusExpr);
		  sign -= 1;
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
				InstrIntf addInstr = new Instr.AddInstr();
				m_compileEnv.addInstr(addInstr);
			} else {
				getProduct();
				// create instruction
				InstrIntf subInstr = new Instr.SubInstr();
				m_compileEnv.addInstr(subInstr);
			}
			token = m_lexer.lookAheadToken(); 
		}
	}

}

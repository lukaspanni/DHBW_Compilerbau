package compiler;

public class ExprReader extends ExprReaderIntf {

	public ExprReader(SymbolTable symbolTable, LexerIntf lexer, CompileEnvIntf compileEnv) throws Exception {
		super(symbolTable, lexer, compileEnv);
	}

	@Override
	public void getAtomicExpr() throws Exception {
		Token token = this.m_lexer.lookAheadToken();
		if (token.m_type == Token.Type.INTEGER) {
			this.m_lexer.advance();
			InstrIntf numberInstr = new Instr.PushNumberInstr(token.m_intValue);
			this.m_compileEnv.addInstr(numberInstr);
		} else if (token.m_type == Token.Type.LPAREN) {
			this.m_lexer.advance();
			getExpr();
			this.m_lexer.expect(Token.Type.RPAREN);
		} else if (token.m_type == Token.Type.IDENT) {
			this.m_lexer.advance();
			InstrIntf variableInstr = new Instr.VariableInstruction(token.m_stringValue);
			this.m_compileEnv.addInstr(variableInstr);
		} else
			throw new ParserException("Unexpected Token: ", token.toString(), this.m_lexer.getCurrentLocationMsg(),
					"numerical expression");
	}

	@Override
	public void getUnaryExpr() throws Exception {
		Token token = this.m_lexer.lookAheadToken();
		boolean neg = false;
		while (token.m_type == Token.Type.MINUS) {
			this.m_lexer.advance();
			neg = !neg;
			token = this.m_lexer.lookAheadToken();
		}
		getAtomicExpr();
		if (neg) {
			InstrIntf unaryMinusInstr = new Instr.unaryMinusInstr();
			this.m_compileEnv.addInstr(unaryMinusInstr);
		}
	}

	@Override
	public void getProduct() throws Exception {
		getUnaryExpr();
		Token token = this.m_lexer.lookAheadToken();
		while (token.m_type == Token.Type.MUL || token.m_type == Token.Type.DIV) {
			this.m_lexer.advance();
			if (token.m_type == Token.Type.MUL) {
				getUnaryExpr();
				InstrIntf mulInstr = new Instr.MulInstr();
				this.m_compileEnv.addInstr(mulInstr);
			} else {
				getUnaryExpr();
				InstrIntf divInstr = new Instr.DivInstr();
				this.m_compileEnv.addInstr(divInstr);
			}
			token = this.m_lexer.lookAheadToken();
		}
	}

	@Override
	public void getExpr() throws Exception {
		getProduct();
		Token token = this.m_lexer.lookAheadToken();
		while (token.m_type == Token.Type.PLUS || token.m_type == Token.Type.MINUS) {
			this.m_lexer.advance();
			if (token.m_type == Token.Type.PLUS) {
				getProduct();
				InstrIntf addInstr = new Instr.AddInstr();
				this.m_compileEnv.addInstr(addInstr);
			} else {
				getProduct();
				InstrIntf subInstr = new Instr.SubInstr();
				this.m_compileEnv.addInstr(subInstr);
			}
			token = this.m_lexer.lookAheadToken();
		}
	}

}

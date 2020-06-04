package compiler;

public class StmtReader implements StmtReaderIntf {
	private SymbolTable m_symbolTable;
	private LexerIntf m_lexer;
	private ExprReader m_exprReader;
	private CompileEnvIntf m_compileEnv;

	public StmtReader(LexerIntf lexer, CompileEnv compileEnv) throws Exception {
		this.m_lexer = lexer;
		this.m_exprReader = new ExprReader(compileEnv.getSymbolTable(), this.m_lexer, compileEnv);
		this.m_compileEnv = compileEnv;
		this.m_symbolTable = compileEnv.getSymbolTable();
	}

	@Override
	public void getStmtList() throws Exception {
		while (this.m_lexer.lookAheadToken().m_type != Token.Type.EOF && this.m_lexer.lookAheadToken().m_type != Token.Type.RBRACE) {
			getStmt();
		}
	}

	@Override
	public void getStmt() throws Exception {
		Token token = this.m_lexer.lookAheadToken();
		if (token.m_type == Token.Type.IDENT) {
			getAssign();
		} else if (token.m_type == Token.Type.PRINT) {
			getPrint();
		} else if (token.m_type == Token.Type.LBRACE) {
			getBlockStmt();
		} else
			throw new ParserException("Unexpected Token: ", token.toString(), this.m_lexer.getCurrentLocationMsg(), "begin of statement");
	}

	@Override
	public void getAssign() throws Exception {
		Token token = this.m_lexer.lookAheadToken();
		String varName = token.m_stringValue;
		this.m_lexer.advance();
		this.m_lexer.expect(Token.Type.ASSIGN);
		this.m_exprReader.getExpr();
		Symbol var = this.m_symbolTable.getOrCreateSymbol(varName);
		this.m_lexer.expect(Token.Type.SEMICOL);
		// create instruction - assignInstruction
	}

	@Override
	public void getPrint() throws Exception {
		this.m_lexer.expect(Token.Type.PRINT);
		this.m_exprReader.getExpr();
		this.m_lexer.expect(Token.Type.SEMICOL);
		// create instruction - printInstruction
	}

	@Override
	public void getBlockStmt() throws Exception {
		this.m_lexer.expect(Token.Type.LBRACE);
		getStmtList();
		this.m_lexer.expect(Token.Type.RBRACE);
	}



}

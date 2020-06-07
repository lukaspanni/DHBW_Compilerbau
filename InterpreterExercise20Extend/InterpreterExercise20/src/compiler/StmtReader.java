package compiler;

public class StmtReader implements StmtReaderIntf {
	private SymbolTable m_symbolTable;
	private LexerIntf m_lexer;
	private ExprReader m_exprReader;
	private CompileEnvIntf m_compileEnv;

	public StmtReader(LexerIntf lexer, CompileEnv compileEnv) throws Exception {
		m_lexer = lexer;
		m_exprReader = new ExprReader(compileEnv.getSymbolTable(), m_lexer, compileEnv);
		m_compileEnv = compileEnv;
		m_symbolTable = compileEnv.getSymbolTable();
	}
	
	public void getStmtList() throws Exception {
		while (m_lexer.lookAheadToken().m_type != Token.Type.EOF && m_lexer.lookAheadToken().m_type != Token.Type.RBRACE) {
			getStmt();
		}
	}

	public void getStmt() throws Exception {
		Token token = m_lexer.lookAheadToken();
		if (token.m_type == Token.Type.IDENT) {
			getAssign();
		} else if (token.m_type == Token.Type.PRINT) {
			getPrint();
		} else if (token.m_type == Token.Type.IF) {
			getIfStmt();
		} else if (token.m_type == Token.Type.LBRACE) {
			getBlockStmt();
		} else {
			throw new ParserException("Unexpected Token: ", token.toString(), m_lexer.getCurrentLocationMsg(), "begin of statement");
		}
	}
	
	public void getAssign() throws Exception {
		Token token = m_lexer.lookAheadToken();
		String varName = token.m_stringValue;
		m_lexer.advance();
		m_lexer.expect(Token.Type.ASSIGN);
		m_exprReader.getExpr();
		Symbol var = m_symbolTable.getOrCreateSymbol(varName);
		m_lexer.expect(Token.Type.SEMICOL);
		// create instruction
		InstrIntf assignInstr = new Instr.AssignInstr(varName);
		m_compileEnv.addInstr(assignInstr);
	}

	public void getPrint() throws Exception {
		m_lexer.advance(); // PRINT
		m_exprReader.getExpr();
		m_lexer.expect(Token.Type.SEMICOL);
		// create instruction
		InstrIntf printInstr = new Instr.PrintInstr();
		m_compileEnv.addInstr(printInstr);
	}

	// ifstmt: IF LPAREN expr RPAREN blockstmt
	public void getIfStmt() throws Exception {
		InstrBlock ifHeadBlock = m_compileEnv.createBlock();
		InstrBlock thenBlock = m_compileEnv.createBlock();
		InstrBlock exitBlock = m_compileEnv.createBlock();
		m_lexer.expect(Token.Type.IF);
		m_lexer.expect(Token.Type.LPAREN);
		InstrIntf jmpHeadInstr = new Instr.JumpInstr(ifHeadBlock);
		m_compileEnv.addInstr(jmpHeadInstr);
		m_compileEnv.setCurrentBlock(ifHeadBlock);
		m_exprReader.getExpr();
		InstrIntf jmpIfCondInstr = new Instr.JumpCondInstr(thenBlock, exitBlock);
		m_compileEnv.addInstr(jmpIfCondInstr);
		m_lexer.expect(Token.Type.RPAREN);
		m_compileEnv.setCurrentBlock(thenBlock);	
		getBlockStmt();
		InstrIntf jmpExitInstr = new Instr.JumpInstr(exitBlock);
		m_compileEnv.setCurrentBlock(exitBlock);	
	}

	public void getBlockStmt() throws Exception {
		m_lexer.expect(Token.Type.LBRACE);
		getStmtList();
		m_lexer.expect(Token.Type.RBRACE);
	}



}

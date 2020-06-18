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
		while (this.m_lexer.lookAheadToken().m_type != Token.Type.EOF
				&& this.m_lexer.lookAheadToken().m_type != Token.Type.RBRACE) {
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
		} else if (token.m_type == Token.Type.IF) {
			getIfStmt();
		} else if (token.m_type == Token.Type.LBRACE) {
			getBlockStmt();
		} else if (token.m_type == Token.Type.WHILE) {
			getWhile();
		} else if (token.m_type == Token.Type.DO) {
			getDoWhile();
		} else
			throw new ParserException("Unexpected Token: ", token.toString(), this.m_lexer.getCurrentLocationMsg(),
					"begin of statement");
	}

	// doWhileStmt: DO whileBlock WHILE whileCondition SEMICOL
	private void getDoWhile() throws Exception {
		this.m_lexer.expect(Token.Type.DO);
		InstrBlock stmtBlock = this.m_compileEnv.createBlock();
		InstrBlock condBlock = this.m_compileEnv.createBlock();
		InstrBlock exitBlock = this.m_compileEnv.createBlock();
		InstrIntf jumpStmt = new Instr.JumpInstr(stmtBlock);
		this.m_compileEnv.addInstr(jumpStmt);
		this.m_compileEnv.setCurrentBlock(stmtBlock);
		getWhileBlock(condBlock);
		this.m_compileEnv.setCurrentBlock(condBlock);
		this.m_lexer.expect(Token.Type.WHILE);
		getWhileCondition(stmtBlock, exitBlock);
		this.m_compileEnv.setCurrentBlock(exitBlock);
		this.m_lexer.expect(Token.Type.SEMICOL);
	}

	// whilStmt: WHILE whileCondition whileBlock
	private void getWhile() throws Exception {
		this.m_lexer.expect(Token.Type.WHILE);
		InstrBlock headBlock = this.m_compileEnv.createBlock();
		InstrBlock stmtBlock = this.m_compileEnv.createBlock();
		InstrBlock exitBlock = this.m_compileEnv.createBlock();
		InstrIntf jumpHead = new Instr.JumpInstr(headBlock);
		this.m_compileEnv.addInstr(jumpHead);
		this.m_compileEnv.setCurrentBlock(headBlock);
		getWhileCondition(stmtBlock, exitBlock);
		this.m_compileEnv.setCurrentBlock(stmtBlock);
		getWhileBlock(headBlock);
		this.m_compileEnv.setCurrentBlock(exitBlock);
	}

	// whileCondition: "("expr")"
	private void getWhileCondition(InstrBlock whileBlock, InstrBlock exitBlock) throws Exception {
		this.m_lexer.expect(Token.Type.LPAREN);
		this.m_exprReader.getExpr();
		InstrIntf conditionalJump = new Instr.ConditionalJumpInstruction(whileBlock, exitBlock);
		this.m_compileEnv.addInstr(conditionalJump);
		this.m_lexer.expect(Token.Type.RPAREN);
	}

	// whileBlock = blockStmt
	private void getWhileBlock(InstrBlock condBlock) throws Exception {
		getBlockStmt();
		InstrIntf jump = new Instr.JumpInstr(condBlock);
		this.m_compileEnv.addInstr(jump);
	}

	public void getIfStmt() throws Exception {
		InstrBlock ifHeadBlock = this.m_compileEnv.createBlock();
		InstrBlock thenBlock = this.m_compileEnv.createBlock();
		InstrBlock exitBlock = this.m_compileEnv.createBlock();
		this.m_lexer.expect(Token.Type.IF);
		this.m_lexer.expect(Token.Type.LPAREN);
		InstrIntf jmpHeadInstr = new Instr.JumpInstr(ifHeadBlock);
		this.m_compileEnv.addInstr(jmpHeadInstr);
		this.m_compileEnv.setCurrentBlock(ifHeadBlock);
		this.m_exprReader.getExpr();
		InstrIntf jmpIfCondInstr = new Instr.ConditionalJumpInstruction(thenBlock, exitBlock);
		this.m_compileEnv.addInstr(jmpIfCondInstr);
		this.m_lexer.expect(Token.Type.RPAREN);
		this.m_compileEnv.setCurrentBlock(thenBlock);
		getBlockStmt();
		InstrIntf jmpExitInstr = new Instr.JumpInstr(exitBlock);
		this.m_compileEnv.setCurrentBlock(exitBlock);
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
		InstrIntf assignInstr = new Instr.AssignInstr(varName);
		this.m_compileEnv.addInstr(assignInstr);
	}

	@Override
	public void getPrint() throws Exception {
		this.m_lexer.expect(Token.Type.PRINT);
		this.m_exprReader.getExpr();
		this.m_lexer.expect(Token.Type.SEMICOL);
		InstrIntf printInstr = new Instr.PrintInstr();
		this.m_compileEnv.addInstr(printInstr);
	}

	@Override
	public void getBlockStmt() throws Exception {
		this.m_lexer.expect(Token.Type.LBRACE);
		getStmtList();
		this.m_lexer.expect(Token.Type.RBRACE);
	}

}

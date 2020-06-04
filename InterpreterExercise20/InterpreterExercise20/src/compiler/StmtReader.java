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
		} else if (token.m_type == Token.Type.WHILE) {
			getWhile();
		} else
			throw new ParserException("Unexpected Token: ", token.toString(), this.m_lexer.getCurrentLocationMsg(), "begin of statement");
	}

	private void getWhile() throws Exception {
		this.m_lexer.expect(Token.Type.WHILE);
		InstrBlock headBlock = this.m_compileEnv.createBlock();
		InstrBlock whileBlock = this.m_compileEnv.createBlock();
		InstrBlock exitBlock = this.m_compileEnv.createBlock();
		InstrIntf jumpHead = new Instr.Jump(headBlock);
		this.m_compileEnv.addInstr(jumpHead);
		this.m_compileEnv.setCurrentBlock(headBlock);
		getWhileHead(headBlock, whileBlock, exitBlock);
		this.m_compileEnv.setCurrentBlock(whileBlock);
		getWhileBlock(whileBlock, headBlock);
		this.m_compileEnv.setCurrentBlock(exitBlock);
	}

	private void getWhileHead(InstrBlock headBlock, InstrBlock whileBlock, InstrBlock exitBlock) throws Exception {
		this.m_lexer.expect(Token.Type.LPAREN);
		this.m_exprReader.getExpr();
		InstrIntf conditionalJump = new Instr.ConditionalJumpInstruction(whileBlock, exitBlock);
		this.m_compileEnv.addInstr(conditionalJump);
		this.m_lexer.expect(Token.Type.RPAREN);
	}

	private void getWhileBlock(InstrBlock whileBlock, InstrBlock headBlock) throws Exception {
		this.m_lexer.expect(Token.Type.LBRACE);
		getStmtList();
		InstrIntf jump = new Instr.Jump(headBlock);
		this.m_compileEnv.addInstr(jump);
		this.m_lexer.expect(Token.Type.RBRACE);

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

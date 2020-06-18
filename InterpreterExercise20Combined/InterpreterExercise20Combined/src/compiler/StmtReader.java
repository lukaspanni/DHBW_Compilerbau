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

	// stmt : assignStmt
	// stmt : printStmt
	// stmt : ifStmt
	// stmt : blockStmt
	// stmt : whileStmt
	// stmt : doWhileStmt
	// stmt : forStmt
	// stmt : callStmt
	// stmt : functionDef
	public void getStmt() throws Exception {
		Token token = m_lexer.lookAheadToken();
		// select
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
        } else if (token.m_type == Token.Type.FOR) {
            getForStmt();
		} else if(token.m_type == Token.Type.CALL) {
			getFunctionCall();
		} else if(token.m_type == Token.Type.FUNCTION){
			new FunctionReader(m_lexer, this, m_compileEnv).getFunction();
		} else {
			throw new ParserException("Unexpected Token: ", token.toString(), m_lexer.getCurrentLocationMsg(), "begin of statement");
		}
	}
	
	// doWhileStmt: DO blockStmt WHILE LPAREN expr RPAREN SEMICOL
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

	// whileStmt: WHILE LPAREN expr RPAREN blockStmt
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

	private void getWhileCondition(InstrBlock whileBlock, InstrBlock exitBlock) throws Exception {
		this.m_lexer.expect(Token.Type.LPAREN);
		this.m_exprReader.getExpr();
		InstrIntf conditionalJump = new Instr.JumpCondInstr(whileBlock, exitBlock);
		this.m_compileEnv.addInstr(conditionalJump);
		this.m_lexer.expect(Token.Type.RPAREN);
	}

	private void getWhileBlock(InstrBlock condBlock) throws Exception {
		getBlockStmt();
		InstrIntf jump = new Instr.JumpInstr(condBlock);
		this.m_compileEnv.addInstr(jump);
	}

    // FOR '(' initializer ';' compare ';' increment ')' getBlockStmt()
    public void getForStmt() throws Exception {

        InstrBlock initializerBlock = m_compileEnv.createBlock();
        InstrBlock compareBlock = m_compileEnv.createBlock();
        InstrBlock incrementBlock = m_compileEnv.createBlock();
        InstrBlock doBlock = m_compileEnv.createBlock();
        InstrBlock exitBlock = m_compileEnv.createBlock();
        m_lexer.expect(Token.Type.FOR);
        m_lexer.expect(Token.Type.LPAREN);

        // beginnend mit initialiser: Einlesen und als Block ausweisen von "i=0"
        InstrIntf jmpHeadInst = new Instr.JumpInstr(initializerBlock);
        m_compileEnv.addInstr(jmpHeadInst);
        m_compileEnv.setCurrentBlock(initializerBlock);
        getAssign();

        // wechsel zu compare-Block: i <= 9
        InstrIntf jmpToComp = new Instr.JumpInstr(compareBlock);
        m_compileEnv.addInstr(jmpToComp);
        m_compileEnv.setCurrentBlock(compareBlock);
        m_exprReader.getExpr();
        m_lexer.expect(Token.Type.SEMICOL);

        InstrIntf jmpToDoBlock = new Instr.JumpCondInstr(doBlock, exitBlock);
        m_compileEnv.addInstr(jmpToDoBlock);

        // wechsel zu increment-Block: i++
        InstrIntf jmpToIncrem = new Instr.JumpInstr(incrementBlock);
        m_compileEnv.setCurrentBlock(incrementBlock);
        getAssign();
        m_lexer.expect(Token.Type.RPAREN);
        m_compileEnv.addInstr(jmpToComp);

        // wechsel in das Blockstmt innerhalb der {}
        m_compileEnv.setCurrentBlock(doBlock);
        getBlockStmt();
        m_compileEnv.addInstr(jmpToIncrem);

        InstrIntf jmpToExit = new Instr.JumpInstr(exitBlock);
        m_compileEnv.addInstr(jmpToExit);
        m_compileEnv.setCurrentBlock(exitBlock);
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
        InstrBlock ifExit = m_compileEnv.createBlock();
        InstrIntf jmpExitInstr = new Instr.JumpInstr(ifExit);
        getIfStmt2(jmpExitInstr);
        m_compileEnv.addInstr(jmpExitInstr);
        m_compileEnv.setCurrentBlock(ifExit);
    }

    private void getIfStmt2(final InstrIntf jmpExitInstr) throws Exception {
        InstrBlock ifHead = m_compileEnv.createBlock();
        InstrBlock ifBody = m_compileEnv.createBlock();
        InstrBlock elseBlock = m_compileEnv.createBlock();
        m_lexer.expect(Token.Type.IF);
        m_lexer.expect(Token.Type.LPAREN);
        InstrIntf jmpHeadInstr = new Instr.JumpInstr(ifHead);
        m_compileEnv.addInstr(jmpHeadInstr);
        m_compileEnv.setCurrentBlock(ifHead);
        m_exprReader.getExpr();
        InstrIntf jmpIfCondInstr = new Instr.JumpCondInstr(ifBody, elseBlock);
        m_compileEnv.addInstr(jmpIfCondInstr);
        m_lexer.expect(Token.Type.RPAREN);
        m_compileEnv.setCurrentBlock(ifBody);
        getBlockStmt();
        System.out.println("EXIT");
        m_compileEnv.addInstr(jmpExitInstr);
        m_compileEnv.setCurrentBlock(elseBlock);
        getElseIfStmt(jmpExitInstr);
    }

    private void getElseStmt() throws Exception {
        InstrBlock ifExit = m_compileEnv.createBlock();
        InstrBlock body = m_compileEnv.createBlock();
        InstrIntf jmpBodyInstr = new Instr.JumpInstr(body);
        m_compileEnv.addInstr(jmpBodyInstr);
        m_compileEnv.setCurrentBlock(body);
        getBlockStmt();
        InstrIntf jmpExitInstr = new Instr.JumpInstr(ifExit);
        m_compileEnv.addInstr(jmpExitInstr);
        m_compileEnv.setCurrentBlock(ifExit);
    }

    public void getElseIfStmt(final InstrIntf jmpExitInstr) throws Exception {
        if (m_lexer.lookAheadToken().m_type == Token.Type.ELSE) {
            m_lexer.expect(Token.Type.ELSE);
            if (m_lexer.lookAheadToken().m_type == Token.Type.IF) {
                System.out.println("GET ELSE IF");
                getIfStmt2(jmpExitInstr);
            } else {
                System.out.println("GET ELSE");
                getElseStmt();
            }
        }
    }


	public void getBlockStmt() throws Exception {
		m_lexer.expect(Token.Type.LBRACE);
		getStmtList();
		m_lexer.expect(Token.Type.RBRACE);
	}
	
	public void getFunctionCall() throws Exception {
		m_lexer.expect(Token.Type.CALL);
		Token functionIdentifier = m_lexer.lookAheadToken();
		m_lexer.expect(Token.Type.IDENT);
		m_lexer.expect(Token.Type.LPAREN);
		getCallArguments();
		m_lexer.expect(Token.Type.RPAREN);
		m_lexer.expect(Token.Type.SEMICOL);
		m_compileEnv.addInstr(new Instr.CallFunctionInstr(m_compileEnv.getFunctionTable().getFunction(functionIdentifier.m_stringValue)));
	}

	public void getCallArguments() throws Exception {
		final Token token = m_lexer.lookAheadToken();

		if(token.m_type == Token.Type.RPAREN) {
			//EPSILON
		} else {
			m_exprReader.getExpr();
			while(m_lexer.lookAheadToken().m_type == Token.Type.COMMA) {
				m_lexer.advance();
				m_exprReader.getExpr();
			}
		}
	}



}

package compiler;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StmtReader implements StmtReaderIntf {
	private SymbolTable symbolTbl;
	private LexerIntf lexer;
	private ExprReader exprReader;
	private OutputStreamWriter outStream;

	public StmtReader(LexerIntf lexer, OutputStream outStream) throws Exception {
		this.symbolTbl = new SymbolTable();
		this.lexer = lexer;
		this.exprReader = new ExprReader(this.symbolTbl, this.lexer);
		this.outStream = new OutputStreamWriter(outStream, "UTF-8");
	}

	@Override
	public void getStmtList() throws Exception {
		while (this.lexer.lookAheadToken().m_type != Token.Type.EOF) {
			getStmt();
		}
		this.outStream.flush();
	}

	@Override
	public void getStmt() throws Exception {
		Token token = this.lexer.lookAheadToken();
		if (token.m_type == Token.Type.IDENT) {
			getAssign();
		} else if (token.m_type == Token.Type.PRINT) {
			getPrint();
		}
	}

	@Override
	public void getAssign() throws Exception {
		Token token = this.lexer.lookAheadToken();
		String name = token.m_stringValue;
		this.lexer.advance();
		this.lexer.expect(Token.Type.ASSIGN);
		int value = this.exprReader.getExpr();
		Symbol var = this.symbolTbl.createSymbol(name, value);
		this.lexer.expect(Token.Type.SEMICOL);
	}

	@Override
	public void getPrint() throws Exception {
		this.lexer.expect(Token.Type.PRINT);
		int value = this.exprReader.getExpr();
		this.outStream.write(Integer.toString(value));
		this.outStream.write('\n');
		this.lexer.expect(Token.Type.SEMICOL);
	}


}

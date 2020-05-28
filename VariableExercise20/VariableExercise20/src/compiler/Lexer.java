package compiler;

public class Lexer implements LexerIntf {
	private FileReaderIntf m_reader;
	private Token m_nextToken;

	public Lexer(FileReaderIntf reader) throws Exception {
		this.m_reader = reader;
		advance();
	}

	@Override
	public Token lookAheadToken() {
		return this.m_nextToken;
	}

	@Override
	public void advance() throws Exception {
		Token.Type tokenType = getTokenType(this.m_reader.lookAheadChar());
		this.m_nextToken = new Token();
		this.m_nextToken.m_type = tokenType;
		if (tokenType == Token.Type.IDENT) {
			String ident = getIdent();
			this.m_nextToken.m_stringValue = ident;
			if (ident.equals("PRINT")) {
				this.m_nextToken.m_type = Token.Type.PRINT;
			}
		} else if (tokenType == Token.Type.INTEGER) {
			int number = getNumber();
			this.m_nextToken.m_intValue = number;
		} else {
			this.m_reader.advance();
		}
		while (isWhiteSpace(this.m_reader.lookAheadChar())) {
			this.m_reader.advance();
		}
	}

	@Override
	public void expect(Token.Type tokenType) throws Exception {
		if (tokenType == this.m_nextToken.m_type) {
			advance();
		} else
			throw new ParserException("Unexpected Token: ", this.m_nextToken.toString(), getCurrentLocationMsg(), Token.type2String(tokenType));
	}

	@Override
	public Token.Type getTokenType(char firstChar) throws Exception {
		if (firstChar == 0)
			return Token.Type.EOF;
		else if (isIdentifierPart(firstChar))
			return Token.Type.IDENT;
		else if ('0' <= firstChar && firstChar <= '9')
			return Token.Type.INTEGER;
		else if (firstChar == '+')
			return Token.Type.PLUS;
		else if (firstChar == '-')
			return Token.Type.MINUS;
		else if (firstChar == '*')
			return Token.Type.MUL;
		else if (firstChar == '/')
			return Token.Type.DIV;
		else if (firstChar == '(')
			return Token.Type.LPAREN;
		else if (firstChar == ')')
			return Token.Type.RPAREN;
		else if (firstChar == '=')
			return Token.Type.ASSIGN;
		else if (firstChar == ';')
			return Token.Type.SEMICOL;
		else
			throw new ParserException("Unexpected character: ", Character.toString(firstChar), this.m_reader.getCurrentLocationMsg(), "");
	}

	private String getIdent() throws Exception {
		String ident = "";
		char nextChar = this.m_reader.lookAheadChar();
		do  {
			ident += nextChar;
			this.m_reader.advance();
			nextChar = this.m_reader.lookAheadChar();
		} while (isIdentifierPart(nextChar));
		return ident;
	}

	@Override
	public boolean isIdentifierPart(char c) {
		boolean isPart = ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
		return isPart;
	}

	private int getNumber() throws Exception {
		int number = 0;
		char nextChar = this.m_reader.lookAheadChar();
		do {
			number *= 10;
			number += nextChar - '0';
			this.m_reader.advance();
			nextChar = this.m_reader.lookAheadChar();
		} while (isDigit(nextChar));
		return number;
	}

	@Override
	public boolean isDigit(char c) {
		return ('0' <= c && c <= '9');
	}

	@Override
	public boolean isWhiteSpace(char c) {
		return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
	}

	@Override
	public String getCurrentLocationMsg() {
		return this.m_reader.getCurrentLocationMsg();
	}
}

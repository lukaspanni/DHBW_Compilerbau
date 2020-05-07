package compiler;

import compiler.TokenIntf.Type;

public class Lexer implements LexerIntf {

	private FileReaderIntf fileReader;
	private Token currentToken;

	public Lexer(FileReaderIntf fileReader) throws Exception {
		this.fileReader = fileReader;
		advance();
	}

	@Override
	public Token lookAheadToken() {
		return this.currentToken;
	}

	@Override
	public void advance() throws Exception {

		this.currentToken = new Token(getTokenType(this.fileReader.lookAheadChar()));
		if (this.currentToken.m_type == Type.INTEGER) {
			this.currentToken.m_intValue = getNumber();
		} else if (this.currentToken.m_type == Type.IDENT) {
			this.currentToken.m_stringValue = getIdent();
		}
		else if (this.currentToken.m_type != Type.EOF) {
			this.fileReader.advance();
		}
		while (isWhiteSpace(this.fileReader.lookAheadChar())) {
			this.fileReader.advance();
		}
	}

	@Override
	public void expect(Token.Type tokenType) throws Exception {
		Type actual = getTokenType(this.fileReader.lookAheadChar());
		if (actual != tokenType)
			throw new ParserException("Token Type doesn't match. Type is:", actual.toString(),
					this.fileReader.getCurrentLocationMsg(), tokenType.toString());
		advance();
	}

	@Override
	public Token.Type getTokenType(char firstChar) throws Exception {
		if (isDigit(firstChar))
			return Type.INTEGER;
		if (firstChar == '+')
			return Type.PLUS;
		if (firstChar == '*')
			return Type.MUL;
		if (firstChar == '(')
			return Type.RPAREN;
		if (firstChar == ')')
			return Type.LPAREN;
		if (firstChar == '=')
			return Type.ASSIGN;
		if (firstChar == 0)
			return Type.EOF;
		if (isIdentifierPart(firstChar))
			return Type.IDENT;
		throw new ParserException("Character not start of Token", "" + firstChar,
				this.fileReader.getCurrentLocationMsg(), "");
	}

	private String getIdent() throws Exception {
		StringBuilder sb = new StringBuilder();
		while (isIdentifierPart(this.fileReader.lookAheadChar())) {
			sb.append(this.fileReader.lookAheadChar());
			this.fileReader.advance();
		}
		return sb.toString();
	}

	private int getNumber() throws Exception {
		int number = 0;
		char nextChar = this.fileReader.lookAheadChar();
		if (!isDigit(nextChar))
			throw new Exception("not a number");
		do {
			this.fileReader.advance();
			number *= 10;
			number += nextChar - '0';
			nextChar = this.fileReader.lookAheadChar();
		} while (isDigit(nextChar));
		return number;
	}

	@Override
	public boolean isIdentifierPart(char c) {
		return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || isDigit(c);
	}

	@Override
	public boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	@Override
	public boolean isWhiteSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n';
	}

}

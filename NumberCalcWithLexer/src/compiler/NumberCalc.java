package compiler;

import compiler.TokenIntf.Type;

public class NumberCalc implements NumberCalcIntf {

	private FileReaderIntf fileReader;
	private Lexer lexer;

	public NumberCalc(FileReaderIntf fileReader) throws Exception {
		this.fileReader = fileReader;
		this.lexer = new Lexer(this.fileReader);
	}

	@Override
	public int getSum() throws Exception {
		int number = getProduct();
		while (this.lexer.lookAheadToken().m_type == Type.PLUS || this.lexer.lookAheadToken().m_type == Type.MINUS) {
			if (this.lexer.lookAheadToken().m_type == Type.PLUS) {
				this.lexer.advance();
				number += getProduct();
			} else {
				this.lexer.advance();
				number -= getProduct();
			}
		}
		return number;
	}

	@Override
	public int getProduct() throws Exception {
		int number = getUnaryMinus();
		while (this.lexer.lookAheadToken().m_type == Type.MUL || this.lexer.lookAheadToken().m_type == Type.DIV) {
			if (this.lexer.lookAheadToken().m_type == Type.MUL) {
				this.lexer.advance();
				number *= getUnaryMinus();
			} else {
				this.lexer.advance();
				number /= getUnaryMinus();
			}

		}
		return number;
	}

	public int getUnaryMinus() throws Exception {
		int number = 0;
		boolean negative = false;
		while (this.lexer.lookAheadToken().m_type == Type.MINUS) {
			this.lexer.advance();
			negative = !negative;
		}
		if (negative) {
			number = -getAtomic();
		} else {
			number = getAtomic();
		}
		return number;
	}

	public int getAtomic() throws Exception {
		int number = 0;
		if (this.lexer.lookAheadToken().m_type == Type.INTEGER) {
			number = this.lexer.lookAheadToken().m_intValue;
			this.lexer.advance();
		} else {
			this.lexer.expect(Type.LPAREN);
			number = getSum();
			this.lexer.expect(Type.RPAREN);
		}
		return number;
	}

}
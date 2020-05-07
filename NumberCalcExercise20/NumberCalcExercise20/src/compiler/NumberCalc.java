package compiler;

public class NumberCalc implements NumberCalcIntf {

	private FileReaderIntf fileReader;
	private NumberReader numberReader;

	public NumberCalc(FileReaderIntf fileReader) {
		this.fileReader = fileReader;
		this.numberReader = new NumberReader(this.fileReader);
	}

	// sum: product ("+" product)*
	// diff: product ("-" product)*
	@Override
	public int getSum() throws Exception {
		int number = getProduct();
		while (this.fileReader.lookAheadChar() == '+' || this.fileReader.lookAheadChar() == '-') {
			char op = this.fileReader.lookAheadChar();
			this.fileReader.advance();
			if (op == '+') {
				number += getProduct();
			} else {
				number -= getProduct();
			}
		}
		return number;
	}

	// product: unaryMinus ("*" unaryMinus)*
	// quotient: unaryMinus ("/" unaryMinus)*
	@Override
	public int getProduct() throws Exception {
		int number = getUnaryMinus();
		while (this.fileReader.lookAheadChar() == '*' || this.fileReader.lookAheadChar() == '/') {
			char op = this.fileReader.lookAheadChar();
			this.fileReader.advance();
			if (op == '*') {
				number *= getUnaryMinus();
			} else {
				number /= getUnaryMinus();
			}
		}
		return number;
	}

	// unaryMinus: -* atomicExpr
	public int getUnaryMinus() throws Exception {
		int number = 0;
		boolean negative = false;
		while (this.fileReader.lookAheadChar() == '-') {
			this.fileReader.advance();
			negative = !negative;
		}
		if (negative) {
			number = -getFactor();
		} else {
			number = getFactor();
		}
		return number;
	}

	// atomicExpr: number | "(" sum ")"
	@Override
	public int getFactor() throws Exception {
		int number = 0;
		if (this.numberReader.isDigit(this.fileReader.lookAheadChar())) {
			number = this.numberReader.getNumber();
		} else {
			this.fileReader.expect('(');
			number = getSum();
			this.fileReader.expect(')');
		}
		return number;
	}

}
package compiler;

public class NumberReader implements NumberReaderIntf {

	private FileReaderIntf fileReader;

	public NumberReader(FileReaderIntf fileReader) {
		this.fileReader = fileReader;
	}

	@Override
	public int getNumber() throws Exception {
		int number = 0;
		char nextChar;
		nextChar = this.fileReader.lookAheadChar();
		if (!isDigit(nextChar))
			throw new Exception(); // don't return 0 if first char isn't a Digit
		while (isDigit(nextChar)) {
			number *= 10;
			number += nextChar - '0';
			this.fileReader.advance();
			nextChar = this.fileReader.lookAheadChar();
		}
		return number;
	}

	@Override
	public boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

}

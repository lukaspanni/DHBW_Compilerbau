package compiler;

public class NumberAdder implements NumberAdderIntf {

	private FileReaderIntf fileReader;
	private NumberReaderIntf numberReader;

	public NumberAdder(FileReaderIntf fileReader) {
		this.fileReader = fileReader;
		this.numberReader = new NumberReader(this.fileReader);
	}

	@Override
	public int getSum() throws Exception {
		int sum, number2;
		sum = this.numberReader.getNumber();
		while (this.fileReader.lookAheadChar() != 0 && this.fileReader.lookAheadChar() != '\r') {
			this.fileReader.expect('+');
			number2 = this.numberReader.getNumber();
			sum = sum + number2;
		}
		return sum;
	}

}

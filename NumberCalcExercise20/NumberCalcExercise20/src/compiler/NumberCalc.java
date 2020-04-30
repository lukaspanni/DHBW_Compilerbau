package compiler;

public class NumberCalc implements NumberCalcIntf {

	private FileReaderIntf fileReader;
	private NumberReader numberReader;

	public NumberCalc(FileReaderIntf fileReader) {
		this.fileReader = fileReader;
		this.numberReader = new NumberReader(this.fileReader);
	}

	@Override
	public int getSum() throws Exception {
		int sum, number2;
		sum = getProduct();
		while (this.fileReader.lookAheadChar() != 0 && this.fileReader.lookAheadChar() != '\r') {
			this.fileReader.expect('+');
			number2 = getProduct();
			sum = sum + number2;
		}
		return sum;
	}

	@Override
	public int getProduct() throws Exception {
		int product, number2;
		product = this.numberReader.getNumber();
		while (this.fileReader.lookAheadChar() != 0 && this.fileReader.lookAheadChar() != '\r') {
			try {
				this.fileReader.expect('*');
			} catch (Exception e) {
				break;
			}
			number2 = this.numberReader.getNumber();
			product = product * number2;
		}
		return product;
	}

	@Override
	public int getFactor() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}

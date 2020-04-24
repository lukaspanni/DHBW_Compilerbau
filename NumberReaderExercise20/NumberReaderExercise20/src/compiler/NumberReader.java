package compiler;

public class NumberReader implements NumberReaderIntf {

	public NumberReader(FileReaderIntf fileReader) {
	}

	@Override
	public int getNumber() throws Exception {
		return 0;
	}

	@Override
	public boolean isDigit(char c) {
		return false;
	}

}

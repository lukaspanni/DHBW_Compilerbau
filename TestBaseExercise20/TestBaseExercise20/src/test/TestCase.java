package test;
import compiler.FileReaderIntf;

public class TestCase implements TestCaseIntf {

	@Override
	public String executeTest(FileReaderIntf fileReader) throws Exception {
		StringBuilder sb = new StringBuilder();
		while (true) {
			char nextChar = fileReader.lookAheadChar();
			if (nextChar == 0) {
				break;
			}
			sb.append(nextChar);
			fileReader.advance();
		}
		return sb.toString();
	}

}

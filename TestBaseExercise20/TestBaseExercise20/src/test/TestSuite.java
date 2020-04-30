package test;

import compiler.FileReaderIntf;

public class TestSuite extends TestSuiteIntf {

	public TestSuite(FileReaderIntf fileReader, TestCaseIntf testCase) {
		super(fileReader, testCase);
	}

	@Override
	void readAndExecuteTestSequence() throws Exception {
		while(this.m_fileReader.lookAheadChar() != 0) {
			readAndExecuteTestCase();
		}
	}

	@Override
	void readAndExecuteTestCase() throws Exception {
		readDollarIn();
		String input = readTestContent();
		readDollarOut();
		String expectedOutput = readTestContent();
		executeTestCase(input, expectedOutput);
	}

	@Override
	String readTestContent() throws Exception {
		StringBuilder sb = new StringBuilder();
		char nextChar = this.m_fileReader.lookAheadChar();
		while (nextChar != '$' && nextChar != 0) {
			sb.append(nextChar);
			this.m_fileReader.advance();
			nextChar = this.m_fileReader.lookAheadChar();
		}
		return sb.toString();

	}

	@Override
	void readDollarIn() throws Exception {
		this.m_fileReader.expect('$');
		this.m_fileReader.expect('I');
		this.m_fileReader.expect('N');
		this.m_fileReader.expect('\r');
		this.m_fileReader.expect('\n');

	}

	@Override
	void readDollarOut() throws Exception {
		this.m_fileReader.expect('$');
		this.m_fileReader.expect('O');
		this.m_fileReader.expect('U');
		this.m_fileReader.expect('T');
		this.m_fileReader.expect('\r');
		this.m_fileReader.expect('\n');

	}

}

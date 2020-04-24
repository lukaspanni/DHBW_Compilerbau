package compiler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileReader implements FileReaderIntf {

	private InputStream inpStream;
	private InputStreamReader streamReader;
	private char lookAheadChar;

	public FileReader(FileInputStream inputStream) throws Exception {
		this.inpStream = inputStream;
		this.streamReader = new InputStreamReader(this.inpStream);
		advance();
	}

	@Override
	public char lookAheadChar() {
		return this.lookAheadChar;
	}

	@Override
	public void advance() throws Exception {
		if (this.streamReader.ready()) {
			this.lookAheadChar = (char) this.streamReader.read();
		} else {
			this.lookAheadChar = 0;
		}
	}

	@Override
	public void expect(char c) throws Exception {
		if(this.lookAheadChar != c)
			throw new Exception();
		advance();
	}

}
package compiler;

import java.io.FileInputStream;
import java.io.InputStream;

public class HelloWorld implements HelloWorldIntf {

	private InputStream inpStream;

	public HelloWorld(FileInputStream inputStream) throws Exception {
		this.inpStream = inputStream;
	}

	@Override
	public String getName() {
		return "TEST";
	}

}

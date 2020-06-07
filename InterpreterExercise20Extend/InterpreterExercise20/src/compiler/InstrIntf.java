package compiler;

import java.io.OutputStreamWriter;

public interface InstrIntf {
	public void execute(ExecutionEnvIntf env);
	public void trace(OutputStreamWriter os) throws Exception;
}

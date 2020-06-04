package compiler;

import java.io.OutputStreamWriter;

public abstract class Instr implements InstrIntf {

	public static class unaryMinusInstr implements InstrIntf {

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op0 = env.popNumber();
			env.pushNumber(-op0);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

	public static class DivInstr implements InstrIntf {

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();
			int result = op0 / op1;
			env.pushNumber(result);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

	public static class MulInstr implements InstrIntf {

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();
			int result = op0 * op1;
			env.pushNumber(result);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

	public static class SubInstr implements InstrIntf {

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();
			int result = op0 - op1;
			env.pushNumber(result);

		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

	public static class AddInstr implements InstrIntf {

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();
			int result = op1 + op0;
			env.pushNumber(result);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

	public static class PrintInstr implements InstrIntf {

		@Override
		public void execute(ExecutionEnvIntf env) {
			System.out.println(env.popNumber());
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

	public static class AssignInstr implements InstrIntf {

		private String m_name;

		public AssignInstr(String varName) {
			this.m_name = varName;
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			Symbol symbol = env.getSymbol(this.m_name);
			symbol.m_number = env.popNumber();
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

	public static class VariableInstruction implements InstrIntf {

		private String m_name;

		public VariableInstruction(String name) {
			this.m_name = name;
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			Symbol symbol = env.getSymbol(this.m_name);
			env.pushNumber(symbol.m_number);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

	public static class PushNumberInstr implements InstrIntf {

		private int m_number;

		public PushNumberInstr(int number) {
			this.m_number = number;
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			env.pushNumber(this.m_number);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}

	}

}

package compiler;

import java.io.IOException;
import java.io.OutputStreamWriter;

public abstract class Instr implements InstrIntf {

	public static class PushNumberInstr implements InstrIntf {
		int m_number;

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

	public static class VariableInstr implements InstrIntf {
		String m_name;

		public VariableInstr(String name) {
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

	public static class AssignInstr implements InstrIntf {
		String m_name;

		public AssignInstr(String name) {
			this.m_name = name;
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

	public static class PrintInstr implements InstrIntf {

		public PrintInstr() {
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			try {
				env.getOutputStream().write(String.valueOf(env.popNumber()) + "\n");
				env.getOutputStream().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class AddInstr implements InstrIntf {

		public AddInstr() {
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();
			int result = op0 + op1;
			env.pushNumber(result);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class SubInstr implements InstrIntf {

		public SubInstr() {
		}

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

	public static class UnaryMinusInstr implements InstrIntf {

		public UnaryMinusInstr() {
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op0 = env.popNumber();
			int result = -op0;
			env.pushNumber(result);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class LogicalBitWiseOrInstr implements InstrIntf {

		public LogicalBitWiseOrInstr() {
			super();
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber(op1 == 1 | op0 == 1 ? 1 : 0);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			// TODO Auto-generated method stub

		}

	}

	public static class MultiplyInstr extends Instr {

		public MultiplyInstr() {
			super();
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber(op1 * op0);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			// TODO Auto-generated method stub

		}

	}

	public static class DivisionInstr extends Instr {

		public DivisionInstr() {
			super();
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber((op0 / op1));
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			// TODO Auto-generated method stub

		}

	}

	public static class LogicalBitWiseAndInstr implements InstrIntf {

		public LogicalBitWiseAndInstr() {
			super();
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber(op1 == 0 | op0 == 0 ? 0 : 1);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			// TODO Auto-generated method stub

		}

	}

	public static class CompareEqualsInstr implements InstrIntf {

		public CompareEqualsInstr() {
			super();
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber(op1 == op0 ? 1 : 0);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			// TODO Auto-generated method stub

		}

	}

	public static class LogicalAndInstr implements InstrIntf {

		public LogicalAndInstr() {
			super();
		}

		// op0 & op1
		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber(op1 == 0 || op0 == 0 ? 0 : 1);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			// TODO Auto-generated method stub

		}

	}

	public static class LogicalOrInstr implements InstrIntf {

		public LogicalOrInstr() {
			super();
		}

		// op0 | op1
		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber(op1 == 1 || op0 == 1 ? 1 : 0);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			// TODO Auto-generated method stub

		}

	}

	public static class CompareGreaterInstr implements InstrIntf {

		public CompareGreaterInstr() {
			super();
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber(op0 > op1 ? 1 : 0);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
		}

	}

	public static class CompareLessInstr implements InstrIntf {

		public CompareLessInstr() {
			super();
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();

			env.pushNumber(op0 < op1 ? 1 : 0);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			// TODO Auto-generated method stub

		}

	}



	public static class JumpInstr implements InstrIntf {
		InstrBlock m_target;

		public JumpInstr(InstrBlock target) {
			this.m_target = target;
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			env.setInstrIter(this.m_target.getIterator());
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class JumpCondInstr implements InstrIntf {
		InstrBlock m_targetTrue;
		InstrBlock m_targetFalse;

		public JumpCondInstr(InstrBlock targetTrue, InstrBlock targetFalse) {
			this.m_targetTrue = targetTrue;
			this.m_targetFalse = targetFalse;
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			int condition = env.popNumber();
			if (condition != 0) {
				env.setInstrIter(this.m_targetTrue.getIterator());
			} else {
				env.setInstrIter(this.m_targetFalse.getIterator());
			}
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	static class InstrReturn extends Instr {

		InstrReturn() {
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			env.popFunction();
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			os.write("RETURN");
			os.write("\n");
		}
	}

	static class InstrPopPar extends Instr {
		private Symbol m_symbol;

		InstrPopPar(Symbol symbol) {
			this.m_symbol = symbol;
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			this.m_symbol.m_number = env.popNumber();
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {
			os.write("PARAMETER: ");
			os.write(this.m_symbol.m_name);
			os.write("\n");
		}
	}

	public static class CallFunctionInstr implements InstrIntf{

		FunctionInfo function;

		CallFunctionInstr(FunctionInfo function){
			this.function = function;
		}

		@Override
		public void execute(ExecutionEnvIntf env) {
			env.pushFunction(this.function);
		}

		@Override
		public void trace(OutputStreamWriter os) throws Exception {

		}
	}


}

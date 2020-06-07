package compiler;

import java.io.OutputStreamWriter;

public abstract class Instr implements InstrIntf {

	public static class PushNumberInstr implements InstrIntf {
		int m_number;
		
		public PushNumberInstr(int number) {
			m_number = number;
		}

		public void execute(ExecutionEnvIntf env) {
			env.pushNumber(m_number);
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}

	}

	public static class VariableInstr implements InstrIntf {
		String m_name;
		
		public VariableInstr(String name) {
			m_name = name;
		}

		public void execute(ExecutionEnvIntf env) {
			Symbol symbol = env.getSymbol(m_name);
			env.pushNumber(symbol.m_number);
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class AssignInstr implements InstrIntf {
		String m_name;
		
		public AssignInstr(String name) {
			m_name = name;
		}

		public void execute(ExecutionEnvIntf env) {
			Symbol symbol = env.getSymbol(m_name);
			symbol.m_number = env.popNumber();
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class PrintInstr implements InstrIntf {
		
		public PrintInstr() {
		}

		public void execute(ExecutionEnvIntf env) {
			System.out.println(env.popNumber());
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class AddInstr implements InstrIntf {
		
		public AddInstr() {
		}

		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();
			int result = op0 + op1;
			env.pushNumber(result);
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class SubInstr implements InstrIntf {
		
		public SubInstr() {
		}

		public void execute(ExecutionEnvIntf env) {
			int op1 = env.popNumber();
			int op0 = env.popNumber();
			int result = op0 - op1;
			env.pushNumber(result);
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class UnaryMinusInstr implements InstrIntf {
		
		public UnaryMinusInstr() {
		}

		public void execute(ExecutionEnvIntf env) {
			int op0 = env.popNumber();
			int result = -op0;
			env.pushNumber(result);
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class JumpInstr implements InstrIntf {
		InstrBlock m_target;
		
		public JumpInstr(InstrBlock target) {
			m_target = target;
		}

		public void execute(ExecutionEnvIntf env) {
			env.setInstrIter(m_target.getIterator());
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

	public static class JumpCondInstr implements InstrIntf {
		InstrBlock m_targetTrue;
		InstrBlock m_targetFalse;
		
		public JumpCondInstr(InstrBlock targetTrue, InstrBlock targetFalse) {
			m_targetTrue = targetTrue;
			m_targetFalse = targetFalse;
		}

		public void execute(ExecutionEnvIntf env) {
			int condition = env.popNumber();			
			if (condition != 0) {
				env.setInstrIter(m_targetTrue.getIterator());
			} else {
				env.setInstrIter(m_targetFalse.getIterator());
			}
		}

		public void trace(OutputStreamWriter os) throws Exception {
		}
	}

}

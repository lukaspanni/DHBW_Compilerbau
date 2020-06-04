import compiler.CompileEnv;

public class InterpreterManualMain {

	// Grammatik:
	// block: "{" stmtlist "}"
	// stmtlist: stmt (stmt)*
	// Stmt: whileStmt | assignStmt | block | printStmt
	// whilStmt: whileHead block
	// whileHead: "while("expr")"
	// expr: product (("+"|"-") product)*
	// product: unaryMinus(("*"|"/") unaryMinus)*
	// unaryMinus: "-"? atomicExpression
	// atomicExpression: "("NUMBER")"

	public static void main(String[] args) throws Exception {
		System.out.println("BEGIN");
		CompileEnv compiler = new CompileEnv(args[0]);
		compiler.compile();
		compiler.execute(System.out);
		System.out.println("END");
	}

}

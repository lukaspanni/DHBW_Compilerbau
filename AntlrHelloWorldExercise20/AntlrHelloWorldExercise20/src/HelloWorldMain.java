
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class HelloWorldMain {

	public static void main(String[] args) throws Exception {
		// create input stream
		CharStream input = CharStreams.fromFileName(args[0]);
		// create lexer
		compiler.HelloWorldLexer lexer = new compiler.HelloWorldLexer(input);
		// create token stream
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create parser
		compiler.HelloWorldParser parser = new compiler.HelloWorldParser(tokens);
		parser.setBuildParseTree(true);
		// build parse tree
		ParseTree tree = parser.greeting();
		// output parse tree
		System.out.println(tree.toStringTree(parser));
		// build tree walker
		ParseTreeWalker walker = new ParseTreeWalker();
		// visit tree
		walker.walk(new HelloWorldPrintListener(), tree);

	}
}

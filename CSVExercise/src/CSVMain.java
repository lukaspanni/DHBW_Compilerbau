
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class CSVMain {

	public static void main(String[] args) throws Exception {
		// create input stream
		CharStream input = CharStreams.fromFileName(args[0]);
		// create lexer
		compiler.csvLexer lexer = new compiler.csvLexer(input);
		// create token stream
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create parser
		compiler.csvParser parser = new compiler.csvParser(tokens);
		parser.setBuildParseTree(true);
		// build parse tree
		ParseTree tree = parser.file();
		// output parse tree
		// System.out.println(tree.toStringTree(parser));
		// build tree walker
		ParseTreeWalker walker = new ParseTreeWalker();
		// visit tree
		walker.walk(new CSVPrintListener(), tree);

	}
}

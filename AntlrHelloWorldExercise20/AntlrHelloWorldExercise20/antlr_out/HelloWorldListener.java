// Generated from .\HelloWorld.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HelloWorldParser}.
 */
public interface HelloWorldListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HelloWorldParser#greeting}.
	 * @param ctx the parse tree
	 */
	void enterGreeting(HelloWorldParser.GreetingContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloWorldParser#greeting}.
	 * @param ctx the parse tree
	 */
	void exitGreeting(HelloWorldParser.GreetingContext ctx);
}
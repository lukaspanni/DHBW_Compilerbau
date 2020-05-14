// Generated from csv.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link csvParser}.
 */
public interface csvListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link csvParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(csvParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link csvParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(csvParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link csvParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(csvParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link csvParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(csvParser.LineContext ctx);
}
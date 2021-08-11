// Generated from net/aequologica/sangiovannilipioni/antlr/SGL2.g4 by ANTLR 4.9.2
package net.aequologica.sangiovannilipioni.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SGL2Parser}.
 */
public interface SGL2Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SGL2Parser#book}.
	 * @param ctx the parse tree
	 */
	void enterBook(SGL2Parser.BookContext ctx);
	/**
	 * Exit a parse tree produced by {@link SGL2Parser#book}.
	 * @param ctx the parse tree
	 */
	void exitBook(SGL2Parser.BookContext ctx);
	/**
	 * Enter a parse tree produced by {@link SGL2Parser#sheet}.
	 * @param ctx the parse tree
	 */
	void enterSheet(SGL2Parser.SheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link SGL2Parser#sheet}.
	 * @param ctx the parse tree
	 */
	void exitSheet(SGL2Parser.SheetContext ctx);
	/**
	 * Enter a parse tree produced by {@link SGL2Parser#sheetid}.
	 * @param ctx the parse tree
	 */
	void enterSheetid(SGL2Parser.SheetidContext ctx);
	/**
	 * Exit a parse tree produced by {@link SGL2Parser#sheetid}.
	 * @param ctx the parse tree
	 */
	void exitSheetid(SGL2Parser.SheetidContext ctx);
	/**
	 * Enter a parse tree produced by {@link SGL2Parser#row}.
	 * @param ctx the parse tree
	 */
	void enterRow(SGL2Parser.RowContext ctx);
	/**
	 * Exit a parse tree produced by {@link SGL2Parser#row}.
	 * @param ctx the parse tree
	 */
	void exitRow(SGL2Parser.RowContext ctx);
	/**
	 * Enter a parse tree produced by {@link SGL2Parser#cell}.
	 * @param ctx the parse tree
	 */
	void enterCell(SGL2Parser.CellContext ctx);
	/**
	 * Exit a parse tree produced by {@link SGL2Parser#cell}.
	 * @param ctx the parse tree
	 */
	void exitCell(SGL2Parser.CellContext ctx);
}
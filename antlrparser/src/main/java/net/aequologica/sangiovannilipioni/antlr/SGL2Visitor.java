// Generated from net/aequologica/sangiovannilipioni/antlr/SGL2.g4 by ANTLR 4.9.2
package net.aequologica.sangiovannilipioni.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SGL2Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SGL2Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SGL2Parser#book}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBook(SGL2Parser.BookContext ctx);
	/**
	 * Visit a parse tree produced by {@link SGL2Parser#sheet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSheet(SGL2Parser.SheetContext ctx);
	/**
	 * Visit a parse tree produced by {@link SGL2Parser#sheetid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSheetid(SGL2Parser.SheetidContext ctx);
	/**
	 * Visit a parse tree produced by {@link SGL2Parser#row}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRow(SGL2Parser.RowContext ctx);
	/**
	 * Visit a parse tree produced by {@link SGL2Parser#cell}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCell(SGL2Parser.CellContext ctx);
}
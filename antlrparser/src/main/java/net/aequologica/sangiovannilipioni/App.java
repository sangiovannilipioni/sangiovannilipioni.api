package net.aequologica.sangiovannilipioni;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.tool.Grammar;

import net.SGL2BaseVisitor;
import net.SGL2Lexer;
import net.SGL2Parser;
import net.SGL2Parser.CellContext;
import net.SGL2Parser.RowContext;
import net.SGL2Parser.SheetContext;
import net.SGL2Parser.SheetidContext;
import net.SGL2Parser.TotalContext;
import net.SGL2Visitor;

class Main {
    static String dir = "../files/";
    static String filename = "Sintesi O2";

    public static void main(String[] args) throws IOException {
        try (Reader reader = new FileReader(dir + filename + ".txt")) {
            CharStream inputStream = CharStreams.fromReader(reader);
            SGL2Lexer sglLexer = new SGL2Lexer(inputStream);
            CommonTokenStream commonTokenStream = new CommonTokenStream(sglLexer);
            SGL2Parser sglParser = new SGL2Parser(commonTokenStream);

            try (PrintWriter w = new PrintWriter(dir + filename + ".json", "UTF-8")) {
                SGLVisitor visitor = new SGLVisitor(w);
                visitor.visitTotal(sglParser.total());
            }
        }
    }

    static class SGLVisitor extends SGL2BaseVisitor<Integer> {

        PrintWriter w;
        boolean prevSheet = false;
        boolean prevRow = false;
        boolean prevCell = false;

        SGLVisitor(PrintWriter w) {
            this.w = w;
        }

        @Override
        public Integer visitTotal(SGL2Parser.TotalContext ctx) {
            w.println("[");
            prevSheet = false;
            Integer ret = super.visitTotal(ctx);
            w.println("]");
            return ret;
        }

        @Override
        public Integer visitSheet(SGL2Parser.SheetContext ctx) {
            if (prevSheet) {
                w.println("\t,{");
            } else {
                w.println("\t{");
            }
            Integer ret = super.visitSheet(ctx);
            w.println("\n\t\t]");
            w.println("\t}");
            prevSheet = true;
            return ret;
        }

        @Override
        public Integer visitSheetid(SGL2Parser.SheetidContext ctx) {
            String con = ctx.CONTENT().getText();
            w.println("\t\t\"sheet\": \""+ cleanString(con) + "\", \n\t\t\"rows\": [");
            prevRow = false;
            Integer ret = super.visitSheetid(ctx);
            return ret;
        }

        @Override
        public Integer visitRow(SGL2Parser.RowContext ctx) {
            if (prevRow) {
                w.print("\t\t\t,[\n");
            } else {
                w.print("\t\t\t[\n");
            }
            prevCell = false;
            Integer ret = super.visitRow(ctx);
            prevRow = true;
            w.println("\t\t\t]");
            return ret;
        }

        @Override
        public Integer visitCell(SGL2Parser.CellContext ctx) {
            String col = ctx.DIGITS().getText();
            String con = ctx.CONTENT().getText();
            if (prevCell) {
                w.print("\t\t\t\t,{");
            } else {
                w.print("\t\t\t\t{");
            }
            w.print("\"col\": " + col + ", \"text\": \""+ cleanString(con) + "\"");
            Integer ret = super.visitCell(ctx);
            w.println(" }");
            prevCell = true;
            return ret;
        }

    }

    public static String cleanString(String qwe) {
        if (qwe.startsWith("|") && qwe.endsWith("|")) {
            qwe = qwe.substring(1, qwe.length() - 1);
        }
        return qwe;
    }

    public static void brutalmain(String[] args) throws IOException {
        System.out.println("Antlr4 brutal Example");
        ParseTree t = brutal(dir + filename + ".txt", "SGL.g4", "sheet");
        try (PrintWriter w = new PrintWriter(dir + filename + ".json", "UTF-8")) {
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new Main.PrintEverything(w), t);
        }
    }

    static ParseTree brutal(String fileName, String combinedGrammarFileName, String startRule) throws IOException {
        final Grammar g = Grammar.load(combinedGrammarFileName);
        LexerInterpreter lexEngine = g.createLexerInterpreter(CharStreams.fromPath(Paths.get(fileName)));
        CommonTokenStream tokens = new CommonTokenStream(lexEngine);
        ParserInterpreter parser = g.createParserInterpreter(tokens);
        ParseTree t = parser.parse(0);

        return t;
    }

    static class PrintEverything implements ParseTreeListener {

        PrintWriter w;
        int inRule = 0;
        int lastRule = -1;
        int max = -1;

        PrintEverything(PrintWriter writer) {
            this.w = writer;
        }

        @Override
        public void visitTerminal(TerminalNode node) {
            String qwe = node.getText();
            if (qwe.startsWith("|") && qwe.endsWith("|")) {
                qwe = qwe.substring(1, qwe.length() - 1);
                qwe = ", \"text\": \"" + qwe + "\"";
            } else if (qwe.length() > 0 && qwe.charAt(0) != '\n') {
                max = Math.max(max, Integer.parseInt(qwe));
                qwe = "\"column\": " + qwe;
            }
            w.println(qwe);
        }

        @Override
        public void visitErrorNode(ErrorNode node) {
            // writer.println("visitErrorNode: " + node.toString());
        }

        @Override
        public void enterEveryRule(ParserRuleContext ctx) {
            inRule = ctx.getRuleIndex();
            if (lastRule == inRule) {
                w.println(",");
            }
            if (ctx.getRuleIndex() == 0) {
                w.println("[");
            } else if (ctx.getRuleIndex() == 1) {
                w.println("[");
            } else if (ctx.getRuleIndex() == 2) {
                w.println("{");
            }
        }

        @Override
        public void exitEveryRule(ParserRuleContext ctx) {
            if (ctx.getRuleIndex() == 0) {
                w.println(", {\"max\": " + max + "}]");
            } else if (ctx.getRuleIndex() == 1) {
                w.println("]");
            } else if (ctx.getRuleIndex() == 2) {
                w.println("}");
            }
            lastRule = ctx.getRuleIndex();
        }

    }
}

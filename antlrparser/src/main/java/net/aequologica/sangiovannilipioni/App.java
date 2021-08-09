package net.aequologica.sangiovannilipioni;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.tool.Grammar;

class Main {
    static String dir = "../files/";
    static String filename = "Sintesi O2";

    public static void main(String[] args) throws IOException {
        System.out.println("Antlr4 Example");
        ParseTree t = parse(dir + filename + ".txt", "SGL.g4", "sheet");
        try (PrintWriter w = new PrintWriter(dir + filename + ".json", "UTF-8")) {
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new Main.PrintEverything(w), t);
        }
    }

    static ParseTree parse(String fileName, String combinedGrammarFileName, String startRule) throws IOException {
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
            } else if (qwe.length() > 0 && qwe.charAt(0) != '\n'){
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
            } else if (ctx.getRuleIndex() == 1){
                w.println("[");
            } else if (ctx.getRuleIndex() == 2){
                w.println("{");
            }
        }

        @Override
        public void exitEveryRule(ParserRuleContext ctx) {
            if (ctx.getRuleIndex() == 0) {
                w.println(", {\"max\": "+max+"}]");
            } else if (ctx.getRuleIndex() == 1){
                w.println("]");
            } else if (ctx.getRuleIndex() == 2){
                w.println("}");
            }
            lastRule = ctx.getRuleIndex();
        }

    }
}

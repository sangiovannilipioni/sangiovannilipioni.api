package net.aequologica.sangiovannilipioni;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import net.SGL2BaseVisitor;
import net.SGL2Lexer;
import net.SGL2Parser;

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
        int max = 0;

        SGLVisitor(PrintWriter w) {
            this.w = w;
        }

        @Override
        public Integer visitTotal(SGL2Parser.TotalContext ctx) {
            w.println("[");
            prevSheet = false;
            max = 0;
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
            w.println("\n\t\t], \n\t\t\"cols\":"+max+"");
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
            max = Math.max(max, Integer.parseInt(col));
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

}

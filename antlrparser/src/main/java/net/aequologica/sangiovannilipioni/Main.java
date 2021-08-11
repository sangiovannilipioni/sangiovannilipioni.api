package net.aequologica.sangiovannilipioni;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import net.aequologica.sangiovannilipioni.antlr.SGL2BaseVisitor;
import net.aequologica.sangiovannilipioni.antlr.SGL2Lexer;
import net.aequologica.sangiovannilipioni.antlr.SGL2Parser;

class Main {
    static String dir = "../files/";
    static String filename = "Sintesi O2";

    /* (see here : https://stackoverflow.com/a/49117903/1070215 for a more generic solution) */
    public static void main(String[] args) throws IOException {
        try (Reader reader = new FileReader(dir + filename + ".txt")) {
            CharStream inputStream = CharStreams.fromReader(reader);
            SGL2Lexer sglLexer = new SGL2Lexer(inputStream);
            CommonTokenStream commonTokenStream = new CommonTokenStream(sglLexer);
            SGL2Parser sglParser = new SGL2Parser(commonTokenStream);

            Writer sw = new StringWriter();
            PrintWriter p = new PrintWriter(sw);
            SGLVisitor visitor = new SGLVisitor(p);
            visitor.visitBook(sglParser.book());
            String unformattedJSON = sw.toString();

            String prettyJSON = new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(unformattedJSON));

            try (PrintWriter w = new PrintWriter(dir + filename + ".json", "UTF-8")) {
                w.print(prettyJSON);
            }
        }
    }

    static class SGLVisitor extends SGL2BaseVisitor<Integer> {

        PrintWriter w;
        boolean prevSheet = false;
        boolean prevRow = false;
        boolean prevCell = false;
        int max = 0;

        final String OPENARRAY   = "[";
        final String CLOSEARRAY  = "]";
        final String OPENOBJECT  = "{";
        final String CLOSEOBJECT = "}";
        final String COLON       = ":";
        final String COMMA       = ",";
        final String QUOTE       = "\"";

        SGLVisitor(PrintWriter w) {
            this.w = w;
        }

        @Override
        public Integer visitBook(SGL2Parser.BookContext ctx) {
            w.print(OPENARRAY);
            this.prevSheet = false;
            this.max = 0;
            Integer ret = super.visitBook(ctx);
            w.print(CLOSEARRAY);
            return ret;
        }

        @Override
        public Integer visitSheet(SGL2Parser.SheetContext ctx) {
            if (this.prevSheet) {
                w.print(COMMA);
            }
            w.print(OPENOBJECT);
            Integer ret = super.visitSheet(ctx);
            w.print(CLOSEARRAY);
            w.print(COMMA);
            writeKey(w, "cols", Integer.toString(this.max));
            w.print(CLOSEOBJECT);
            this.prevSheet = true;
            return ret;
        }

        @Override
        public Integer visitSheetid(SGL2Parser.SheetidContext ctx) {
            String con = ctx.CONTENT().getText();
            writeKey(w, "sheet", con);
            w.print(COMMA);
            writeKey(w, "rows", null);
            w.print(OPENARRAY);
            this.prevRow = false;
            Integer ret = super.visitSheetid(ctx);
            return ret;
        }

        @Override
        public Integer visitRow(SGL2Parser.RowContext ctx) {
            if (this.prevRow) {
                w.print(COMMA);
            }
            w.print(OPENARRAY);
            this.prevCell = false;
            Integer ret = super.visitRow(ctx);
            this.prevRow = true;
            w.print(CLOSEARRAY);
            return ret;
        }

        @Override
        public Integer visitCell(SGL2Parser.CellContext ctx) {
            String col = ctx.DIGITS().getText();
            String con = ctx.CONTENT().getText();
            if (this.prevCell) {
                w.print(COMMA);
            }
            w.print(OPENOBJECT);
            this.max = Math.max(max, Integer.parseInt(col));
            writeKey(w, "col", col);
            w.print(COMMA);
            writeKey(w, "text", con);
            Integer ret = super.visitCell(ctx);
            w.print(CLOSEOBJECT);
            this.prevCell = true;
            return ret;
        }

        void writeKey(PrintWriter w, String key, String value) {
            w.print(QUOTE);
            w.print(key);
            w.print(QUOTE);
            w.print(COLON);
            if (value != null) {
                w.print(QUOTE);
                w.print(cleanString(value));
                w.print(QUOTE);
            }
        }
    }


    public static String cleanString(String str) {
        if (str.startsWith("|") && str.endsWith("|")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

}

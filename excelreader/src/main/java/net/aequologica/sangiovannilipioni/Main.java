package net.aequologica.sangiovannilipioni;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
/* UN-COMMENT TO MAP CELLTYPES */
// import java.util.HashMap;
import java.util.Iterator;
/* UN-COMMENT TO MAP CELLTYPES */
// import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static void main(String[] args) throws IOException, FileNotFoundException, UnsupportedEncodingException {
        String dir = "../files/";
        String filename = "Sintesi O2";

        File fileIn = new File(dir, filename + ".xlsx");
        File fileOut = new File(dir, filename + ".txt");

        final String SEP = "|";

        /*
         * UN-COMMENT TO MAP CELLTYPES (in case something else than NUMERIC or STRING or
         * BLANK comes in)
         */
        // Map<String, CellType> cellTypes = new HashMap<String, CellType>();
        try (FileInputStream excelFile = new FileInputStream(fileIn)) {
            try (PrintWriter out = new PrintWriter(fileOut, "UTF-8")) {
                try (Workbook book = new XSSFWorkbook(excelFile)) {

                    Iterator<Sheet> sheetIterator = book.iterator();

                    while (sheetIterator.hasNext()) {
                        Sheet sheet = sheetIterator.next();
                        out.print("-1|");
                        out.print(sheet.getSheetName());
                        out.println("|");
                        Iterator<Row> rowIterator = sheet.iterator();

                        while (rowIterator.hasNext()) {

                            Row row = rowIterator.next();
                            Iterator<Cell> cellIterator = row.iterator();

                            boolean printLine = false;
                            while (cellIterator.hasNext()) {

                                Cell cell = cellIterator.next();
                                CellType type = cell.getCellType();
                                /* UN-COMMENT TO MAP CELLTYPES */
                                // cellTypes.put(type.name(), type); writer.print(type);

                                if (cell.getCellType() == STRING) {
                                    String str = nolinefeed_nodoublequote_trim(cell.getStringCellValue());
                                    if (str.length() > 0) {
                                        out.print(cell.getColumnIndex() + SEP + str + SEP);
                                        printLine = true;
                                    }
                                } else if (cell.getCellType() == NUMERIC) {
                                    double number = cell.getNumericCellValue();
                                    out.print(cell.getColumnIndex() + SEP + number + SEP);
                                    printLine = true;
                                } else if (type.equals(CellType.BLANK)) {
                                }
                            }
                            if (printLine) {
                                out.println();
                            }
                        }
                    }
                }
                /* UN-COMMENT TO MAP CELLTYPES */
                // for (Map.Entry<String, CellType> entry : cellTypes.entrySet()) {
                // System.out.println(entry.getKey() + " : " + entry.getValue());
                // }

            }
        }
    }

    static String nolinefeed_nodoublequote_trim(String str) {
        return str.replace("\n", "").replace("\r", "") // no line feed
                .replace("\"", "'") // no double quote
                .trim();
    }
}

package net.aequologica.sangiovannilipioni;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
// import java.util.HashMap;
import java.util.Iterator;
// import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        String dir = "../files/";
        String filename = "Sintesi O2";
        FileInputStream excelFile = new FileInputStream(new File(dir + filename + ".xlsx"));

        /* Map<String, CellType> cellTypes = new HashMap<String, CellType>(); */
        try (PrintWriter writer = new PrintWriter(dir + filename + ".txt", "UTF-8")) {
            try (Workbook workbook = new XSSFWorkbook(excelFile)) {

                Iterator<Sheet> sheetIterator = workbook.iterator();

                while (sheetIterator.hasNext()) {
                    Sheet currentSheet = sheetIterator.next();
                    writer.print("0æ");
                    writer.print(currentSheet.getSheetName());
                    writer.println("œ");
                    Iterator<Row> rowIterator = currentSheet.iterator();

                    while (rowIterator.hasNext()) {

                        Row currentRow = rowIterator.next();
                        Iterator<Cell> cellIterator = currentRow.iterator();

                        boolean printLine = false;
                        Integer col = 0;
                        while (cellIterator.hasNext()) {

                            Cell currentCell = cellIterator.next();
                            CellType cellType = currentCell.getCellType();
                            if (!cellType.equals(CellType.BLANK)) {
                                /*
                                 * cellTypes.put(cellType.name(), cellType); writer.print(cellType);
                                 */
                                if (currentCell.getCellType() == STRING) {
                                    String str = nolfnodoublequotetrim(currentCell.getStringCellValue());
                                    if (str.length() > 0) {
                                        writer.print(col.toString() + "æ" + str + "œ");
                                        printLine = true;
                                    }
                                } else if (currentCell.getCellType() == NUMERIC) {
                                    writer.print(col.toString() + "æ" + currentCell.getNumericCellValue() + "œ");
                                    printLine = true;
                                }
                            }
                            col++;
                        }
                        if (printLine) {
                            writer.println();
                        }
                    }
                }
            }
            /*
             * for (Map.Entry<String, CellType> entry : cellTypes.entrySet()) {
             * System.out.println(entry.getKey() + "/" + entry.getValue()); }
             */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String nolfnodoublequotetrim(String in) {
        return in.replace("\n", "").replace("\"", "'").replace("\r", "").trim();
    }
}

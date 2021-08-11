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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        String dir = "../files/";
        String filename = "Sintesi O2";
        FileInputStream excelFile = new FileInputStream(new File(dir + filename + ".xlsx"));

        /* UN-COMMENT TO MAP CELLTYPES (in case something else than NUMERIC or STRING or BLANK comes in) */
        // Map<String, CellType> cellTypes = new HashMap<String, CellType>(); 
        try (PrintWriter writer = new PrintWriter(dir + filename + ".txt", "UTF-8")) {
            try (Workbook workbook = new XSSFWorkbook(excelFile)) {

                Iterator<Sheet> sheetIterator = workbook.iterator();

                while (sheetIterator.hasNext()) {
                    Sheet currentSheet = sheetIterator.next();
                    writer.print("-1|");
                    writer.print(currentSheet.getSheetName());
                    writer.println("|");
                    Iterator<Row> rowIterator = currentSheet.iterator();

                    while (rowIterator.hasNext()) {

                        Row currentRow = rowIterator.next();
                        Iterator<Cell> cellIterator = currentRow.iterator();

                        boolean printLine = false;
                        while (cellIterator.hasNext()) {

                            Cell currentCell = cellIterator.next();
                            CellType cellType = currentCell.getCellType();
                            /* UN-COMMENT TO MAP CELLTYPES */
                            /*
                            * cellTypes.put(cellType.name(), cellType); writer.print(cellType);
                            */
                                
                            if (currentCell.getCellType() == STRING) {
                                String str = nolfnodoublequotetrim(currentCell.getStringCellValue());
                                writer.print(currentCell.getColumnIndex() + "|" + str + "|");
                                printLine = true;
                            } else if (currentCell.getCellType() == NUMERIC) {
                                writer.print(currentCell.getColumnIndex()  + "|" + currentCell.getNumericCellValue() + "|");
                                printLine = true;
                            } else if (!cellType.equals(CellType.BLANK)) {
                            }
                        }
                        if (printLine) {
                            writer.println();
                        }
                    }
                }
            }
            /* UN-COMMENT TO MAP CELLTYPES */
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

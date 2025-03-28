package Matcher;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.*;

public class Companies {
    private String filePath;
    private ExcelReader excelReader;

    public Companies(String filePath, ExcelReader excelReader) {
        this.filePath = filePath;
        this.excelReader = excelReader;
    }

    public void updateExcelWithMatchedLabels() {
        Map<String, Set<String>> labelMapWords = excelReader.readLemmatizedLabels();

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            int matchedLabelsColumnIndex = -1;

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                if (headerRow.getCell(i) != null && headerRow.getCell(i).toString().trim().equalsIgnoreCase("Matched Labels")) {
                    matchedLabelsColumnIndex = i;
                    break;
                }
            }
            if (matchedLabelsColumnIndex == -1) {
                matchedLabelsColumnIndex = headerRow.getLastCellNum();
                headerRow.createCell(matchedLabelsColumnIndex).setCellValue("Matched Labels");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String companyName = getCellValue(row, 0).trim();
                String description = getCellValue(row, 1);
                String businessTags = getCellValue(row, 2);
                String sector = getCellValue(row, 3);
                String category = getCellValue(row, 4);
                String niche = getCellValue(row, 5);

                String combinedTxt = description + " " + businessTags + " " + sector + " " + category + " " + niche;
                OpenNLPLematizer lemmatizer = new OpenNLPLematizer(combinedTxt);
                Set<String> words = new HashSet<>(Arrays.asList(lemmatizer.lemmatizeText().split(" ")));

                List<String> matchedLabels = new ArrayList<>();
                for (Map.Entry<String, Set<String>> entry : labelMapWords.entrySet()) {
                    String label = entry.getKey();
                    Set<String> labelWords = entry.getValue();
                    for (String word : labelWords) {
                        if (words.contains(word)) {
                            matchedLabels.add(label);
                            break;
                        }
                    }
                }

                Cell cell = row.getCell(matchedLabelsColumnIndex);
                if (cell == null) {
                    cell = row.createCell(matchedLabelsColumnIndex);
                }
                cell.setCellValue(String.join(", ", matchedLabels));
            }

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                System.out.println("✅ Matched labels have been successfully saved to Excel.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCellValue(Row row, int index) {
        return row.getCell(index) != null ? row.getCell(index).toString().toLowerCase() : "";
    }
}

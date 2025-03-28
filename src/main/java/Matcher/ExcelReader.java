package Matcher;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelReader {
    private String filePath;
    private String lemmatizedLabelsFile;
    private Map<String, Set<String>> labelMapWords;

    public ExcelReader(String filepath, String lemmatizedLabelsFile) {
        this.filePath = filepath;
        this.lemmatizedLabelsFile = lemmatizedLabelsFile;
        this.labelMapWords = new HashMap<>();
    }


    public void preprocessLabels() {
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file);
             BufferedWriter writer = new BufferedWriter(new FileWriter(lemmatizedLabelsFile))) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                for (Row row : sheet) {
                    StringBuilder rowContent = new StringBuilder();
                    for (Cell cell : row) {
                        rowContent.append(cell.toString().toLowerCase()).append(" ");
                    }

                    String rowText = rowContent.toString().trim();
                    if (!rowText.isEmpty()) {
                        OpenNLPLematizer lemmatizer = new OpenNLPLematizer(rowText);
                        String lemmatizedText = lemmatizer.lemmatizeText();
                        writer.write(rowText + "\t" + lemmatizedText);
                        writer.newLine();


                        String[] parts = lemmatizedText.split("\t", 2);
                        if (parts.length > 1) {
                            String label = parts[0].trim();
                            Set<String> words = new HashSet<>(Arrays.asList(parts[1].split(" ")));
                            labelMapWords.put(label, words);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Map<String, Set<String>> getLabelMapWords() {
        return labelMapWords;
    }


    public Map<String, Set<String>> readLemmatizedLabels() {
        Map<String, Set<String>> labelMapWords = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(lemmatizedLabelsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t", 2);
                if (parts.length < 2) continue;

                String label = parts[0];
                Set<String> words = new HashSet<>(Arrays.asList(parts[1].split(" ")));
                labelMapWords.put(label, words);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labelMapWords;
    }
}

package com.example;


import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "E:\\Job\\ml_insurance_challenge.xlsx";

        // Create an instance of ExcelReader and read the file
        ExcelReader file1 = new ExcelReader(filePath);
        file1.readFile();  // Read and process the file, storing words in labelMapWords

        // Create an instance of Companies and pass the file and the ExcelReader instance
        Companies file2 = new Companies(filePath, file1);

        // Get and print the matched companies with their labels
        Map<String, List<String>> matchedCompanies = file2.matchCompanies();
        System.out.println("Matched Companies and their Labels:");
        for (Map.Entry<String, List<String>> entry : matchedCompanies.entrySet()) {
            System.out.println("Company: " + entry.getKey() + ", Matched Labels: " + entry.getValue());
        }
    }
}
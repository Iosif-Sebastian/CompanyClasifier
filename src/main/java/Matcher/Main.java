package Matcher;

import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String labelsFile = "E:\\Job\\insurance_taxonomy.xlsx";
        String companiesFile = "E:\\Job\\ml_insurance_challenge.xlsx";
        String lemmatizedLabelsFile = "E:\\Job\\labels_lemmatized.txt";

        ExcelReader labelsProcessor = new ExcelReader(labelsFile, lemmatizedLabelsFile);
        labelsProcessor.preprocessLabels();
        Map<String, Set<String>> labelMapWords = labelsProcessor.readLemmatizedLabels();

        Companies companiesProcessor = new Companies(companiesFile, labelsProcessor);
        companiesProcessor.updateExcelWithMatchedLabels();
    }
}
package com.example;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.io.*;
import java.util.StringJoiner;

public class OpenNLPLematizer {
    private static DictionaryLemmatizer lemmatizer;
    private final String text;

    // Static block to load the dictionary
    static {
        try {
            // Load the lemmatizer dictionary
            InputStream lemmatizerStream = new FileInputStream("src/main/resources/en-lemmatizer.dict");
            lemmatizer = new DictionaryLemmatizer(lemmatizerStream);
            lemmatizerStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Constructor that accepts a text to lemmatize
    public OpenNLPLematizer(String text) {
        this.text = text; // Initialize the 'text' variable
    }

    // Method to lemmatize the provided text
    public String lemmatizeText() {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;  // Tokenize the text
        String[] tokens = tokenizer.tokenize(text);

        // The POS tags here are dummy values because we are not using them for lemmatization
        String[] dummyPosTags = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            dummyPosTags[i] = "NN";  // Assuming all words are noun-like for lemmatization
        }

        String[] lemmas = lemmatizer.lemmatize(tokens, dummyPosTags);

        // Create the lemmatized text as a space-separated string
        StringJoiner lemmatizedText = new StringJoiner(" ");
        for (String lemma : lemmas) {
            lemmatizedText.add(lemma.equals("O") ? "-" : lemma);  // If lemmatized result is 'O', replace with "-"
        }
        return lemmatizedText.toString();  // Return the lemmatized text
    }
}

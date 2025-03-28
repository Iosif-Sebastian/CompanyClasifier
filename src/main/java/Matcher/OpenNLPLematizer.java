package Matcher;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.io.*;
import java.util.StringJoiner;

public class OpenNLPLematizer {
    private static DictionaryLemmatizer lemmatizer;
    private final String text;

    static {
        try (InputStream lemmatizerStream = new FileInputStream("src/main/resources/en-lemmatizer.dict")) {
            lemmatizer = new DictionaryLemmatizer(lemmatizerStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading lemmatizer dictionary", e);
        }
    }

    public OpenNLPLematizer(String text) {
        this.text = text;
    }

    public String lemmatizeText() {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(text);

        String[] posTags = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            posTags[i] = "NN";
        }

        String[] lemmas = lemmatizer.lemmatize(tokens, posTags);
        StringJoiner lemmatizedText = new StringJoiner(" ");
        for (int i = 0; i < lemmas.length; i++) {
            lemmatizedText.add(lemmas[i].equals("O") ? tokens[i] : lemmas[i]);
        }
        return lemmatizedText.toString();
    }
}

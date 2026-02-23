package com.andy.promptopt;

public class PromptAnalyzer {
    public AnalysisResult analyze(String input) {
        String safeInput = input == null ? "" : input;
        int charCount = safeInput.length();
        int wordCount = countWords(safeInput);
        return new AnalysisResult(safeInput, charCount, wordCount);
    }

    private int countWords(String input) {
        int count = 0;
        boolean inWord = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c)) {
                inWord = false;
            } else if (!inWord) {
                count++;
                inWord = true;
            }
        }
        return count;
    }
}

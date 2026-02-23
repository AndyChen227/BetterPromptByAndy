package com.andy.promptopt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PromptAnalyzerTest {
    @Test
    void analyze_countsCharsAndWords() {
        PromptAnalyzer analyzer = new PromptAnalyzer();
        AnalysisResult result = analyzer.analyze("Hello world");
        assertEquals("Hello world", result.input());
        assertEquals(11, result.charCount());
        assertEquals(2, result.wordCount());
    }
}

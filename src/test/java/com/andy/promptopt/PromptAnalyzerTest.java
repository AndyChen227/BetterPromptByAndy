package com.andy.promptopt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromptAnalyzerTest {
    @Test
    void analyze_countsCharsAndWords() {
        PromptAnalyzer analyzer = new PromptAnalyzer();
        AnalysisResult result = analyzer.analyze("Hello world");
        assertEquals("Hello world", result.input());
        assertEquals(11, result.charCount());
        assertEquals(2, result.wordCount());
    }

    @Test
    void analyze_detectsLinearAlgebraExplain() {
        PromptAnalyzer analyzer = new PromptAnalyzer();
        AnalysisResult result = analyzer.analyze("what is null space");
        assertEquals(Domain.LINEAR_ALGEBRA, result.domain());
        assertEquals(TaskType.EXPLAIN, result.taskType());
        assertTrue(result.domainConfidence() >= 0.45);
        assertTrue(result.taskConfidence() >= 0.45);
    }

    @Test
    void analyze_detectsPhysics() {
        PromptAnalyzer analyzer = new PromptAnalyzer();
        AnalysisResult result = analyzer.analyze("Gauss law example");
        assertEquals(Domain.PHYSICS, result.domain());
        assertTrue(result.domainConfidence() >= 0.45);
    }

    @Test
    void analyze_detectsCodingDebug() {
        PromptAnalyzer analyzer = new PromptAnalyzer();
        AnalysisResult result = analyzer.analyze("java hashmap NullPointerException");
        assertEquals(Domain.CODING, result.domain());
        assertEquals(TaskType.DEBUG, result.taskType());
        assertTrue(result.domainConfidence() >= 0.45);
        assertTrue(result.taskConfidence() >= 0.45);
    }
}

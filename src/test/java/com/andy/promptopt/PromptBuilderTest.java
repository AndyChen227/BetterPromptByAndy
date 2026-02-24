package com.andy.promptopt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PromptBuilderTest {
    @Test
    void buildPrompt_containsExpectedSections() {
        PromptBuilder builder = new PromptBuilder();
        AnalysisResult result = new AnalysisResult(
                "Hi",
                2,
                1,
                Domain.CODING,
                0.8,
                TaskType.DEBUG,
                0.9,
                java.util.List.of("java", "error"),
                java.util.Map.of(Domain.CODING, 10),
                java.util.Map.of(TaskType.DEBUG, 10)
        );
        String markdown = builder.buildPrompt(result);
        assertTrue(markdown.contains("## Context"));
        assertTrue(markdown.contains("## Task"));
        assertTrue(markdown.contains("## Constraints"));
        assertTrue(markdown.contains("## Output Format"));

        long headingCount = markdown.lines().filter(line -> line.startsWith("## ")).count();
        assertEquals(4, headingCount);
    }
}

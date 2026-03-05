package com.andy.promptopt;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.build.PromptBuilder;
import com.andy.promptopt.model.Domain;
import com.andy.promptopt.model.TaskType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        assertFalse(markdown.contains("## Constraints"));
        assertFalse(markdown.contains("## Output Format"));

        long headingCount = markdown.lines().filter(line -> line.startsWith("## ")).count();
        assertEquals(2, headingCount);
    }
}

package com.andy.promptopt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PromptBuilderTest {
    @Test
    void buildPrompt_containsExpectedSections() {
        PromptBuilder builder = new PromptBuilder();
        AnalysisResult result = new AnalysisResult("Hi", 2, 1);
        String markdown = builder.buildPrompt(result);
        assertTrue(markdown.contains("# Prompt Analysis"));
        assertTrue(markdown.contains("## Input"));
        assertTrue(markdown.contains("## Stats"));
        assertTrue(markdown.contains("Characters: 2"));
        assertTrue(markdown.contains("Words: 1"));
    }
}

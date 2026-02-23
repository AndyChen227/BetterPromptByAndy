package com.andy.promptopt;

public class PromptBuilder {
    public String buildPrompt(AnalysisResult result) {
        String input = result == null ? "" : result.input();
        int charCount = result == null ? 0 : result.charCount();
        int wordCount = result == null ? 0 : result.wordCount();

        StringBuilder sb = new StringBuilder();
        sb.append("# Prompt Analysis\n\n");
        sb.append("## Input\n\n");
        sb.append("```\n");
        sb.append(input);
        sb.append("\n```\n\n");
        sb.append("## Stats\n\n");
        sb.append("- Characters: ").append(charCount).append("\n");
        sb.append("- Words: ").append(wordCount).append("\n");
        return sb.toString();
    }
}

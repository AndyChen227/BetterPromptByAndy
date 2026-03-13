package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Intent;

import java.util.Locale;

public class ClarifyQuestionsRule implements Rule {
    private static final String[] KEYWORDS = {
            "explain", "write", "summarize", "compare", "generate",
            "fix", "debug", "design", "analyze"
    };

    private static final String QUESTIONS_BLOCK = """
            
            
            ## Clarifying Questions
            - What is your goal or expected outcome?
            - Who is the target audience (beginner/intermediate/advanced)?
            - What output format do you want (text/markdown/code/examples)?
            """;

    private static final String REASON =
            "Input is too short or missing a clear task, so clarification is needed.";

    @Override
    public String id() {
        return "R_CLARIFY_QUESTIONS";
    }

    @Override
    public String name() {
        return "Ask clarifying questions when input is vague";
    }

    @Override
    public boolean matches(String rawInput, AnalysisResult analysis) {
        String trimmed = rawInput == null ? "" : rawInput.trim();
        return switch (analysis.taskType()) {
            case DEBUG -> analysis.intent() == Intent.TROUBLESHOOTING;
            case PLAN -> analysis.intent() == Intent.ACTION_PLAN;
            case GENERAL -> true;
            case SOLVE -> analysis.intent() == Intent.IMPLEMENTATION && isUnderspecified(trimmed, analysis.wordCount());
            case EXPLAIN, SUMMARIZE, PROVE -> false;
        };
    }

    private boolean isUnderspecified(String trimmed, int wordCount) {
        if (trimmed.length() < 25 || wordCount <= 4) {
            return true;
        }

        String lower = trimmed.toLowerCase(Locale.ROOT);
        for (String keyword : KEYWORDS) {
            if (lower.contains(keyword)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public RuleResult apply(String builtPrompt, String rawInput, AnalysisResult analysis) {
        return new RuleResult(builtPrompt + QUESTIONS_BLOCK, REASON);
    }
}

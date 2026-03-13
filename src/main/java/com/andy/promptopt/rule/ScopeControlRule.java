package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Intent;

public class ScopeControlRule implements Rule {
    @Override
    public String id() {
        return "R_SCOPE_CONTROL";
    }

    @Override
    public String name() {
        return "Control answer scope";
    }

    @Override
    public boolean matches(String rawInput, AnalysisResult analysis) {
        return switch (analysis.taskType()) {
            case EXPLAIN -> analysis.intent() == Intent.CONCEPT || analysis.intent() == Intent.COMPARISON;
            case DEBUG -> analysis.intent() == Intent.TROUBLESHOOTING;
            case PLAN -> analysis.intent() == Intent.LEARNING || analysis.intent() == Intent.ACTION_PLAN;
            case SUMMARIZE -> analysis.intent() == Intent.SUMMARY;
            case SOLVE, PROVE, GENERAL -> false;
        };
    }

    @Override
    public RuleResult apply(String builtPrompt, String rawInput, AnalysisResult analysis) {
        if (builtPrompt.contains("## Scope Control")) {
            return new RuleResult(builtPrompt, "Scope guidance is already present.");
        }

        StringBuilder sb = new StringBuilder(builtPrompt);
        if (!builtPrompt.endsWith("\n")) {
            sb.append("\n");
        }

        sb.append("\n## Scope Control\n");
        sb.append("- Focus on the user's requested topic.\n");
        sb.append("- Avoid unrelated details or side topics.\n");
        sb.append("- Keep the response aligned with the stated goal.\n");

        return new RuleResult(sb.toString(), "Scope control guidance added.");
    }
}

package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Intent;

import java.util.Locale;

public class StepByStepRule implements Rule {
    @Override
    public String id() {
        return "R_STEP_BY_STEP";
    }

    @Override
    public String name() {
        return "Add step-by-step reasoning guidance";
    }

    @Override
    public boolean matches(String rawInput, AnalysisResult analysis) {
        return switch (analysis.taskType()) {
            case PLAN, PROVE, SOLVE -> true;
            case DEBUG -> analysis.intent() == Intent.TROUBLESHOOTING;
            case EXPLAIN, SUMMARIZE, GENERAL -> false;
        };
    }

    @Override
    public RuleResult apply(String builtPrompt, String rawInput, AnalysisResult analysis) {
        String lower = builtPrompt.toLowerCase(Locale.ROOT);
        if (builtPrompt.contains("## Reasoning Process")
                || lower.contains("step-by-step")
                || lower.contains("step by step")) {
            return new RuleResult(builtPrompt, "Reasoning guidance already present.");
        }

        StringBuilder sb = new StringBuilder(builtPrompt);
        if (!builtPrompt.endsWith("\n")) {
            sb.append("\n");
        }

        sb.append("\n## Reasoning Process\n");
        switch (analysis.domain()) {
            case LINEAR_ALGEBRA -> {
                sb.append("- Explain each step in a clear logical order.\n");
                sb.append("- Show how definitions or theorems are used.\n");
                sb.append("- Do not skip algebraic steps when they are important.\n");
            }
            case PHYSICS -> {
                sb.append("- Explain each step in a clear physical and mathematical order.\n");
                sb.append("- State formulas before applying them.\n");
                sb.append("- Do not skip important calculation steps.\n");
            }
            default -> {
                sb.append("- Break the response into clear steps.\n");
                sb.append("- Explain the reasoning in a logical order.\n");
                sb.append("- Do not skip important intermediate steps.\n");
            }
        }

        return new RuleResult(sb.toString(), "Reasoning process guidance added.");
    }
}

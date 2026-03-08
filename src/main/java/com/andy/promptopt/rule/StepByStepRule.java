package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;

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
        Domain domain = analysis.domain();
        return domain == Domain.LINEAR_ALGEBRA || domain == Domain.PHYSICS;
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
                return new RuleResult(builtPrompt, "Reasoning guidance not added because domain is not supported.");
            }
        }

        return new RuleResult(sb.toString(), "Reasoning process guidance added.");
    }
}

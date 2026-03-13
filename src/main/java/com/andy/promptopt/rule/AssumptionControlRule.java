package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Intent;

public class AssumptionControlRule implements Rule {
    @Override
    public String id() {
        return "R_ASSUMPTION_CONTROL";
    }

    @Override
    public String name() {
        return "Reduce unsupported assumptions";
    }

    @Override
    public boolean matches(String rawInput, AnalysisResult analysis) {
        return switch (analysis.taskType()) {
            case DEBUG -> analysis.intent() == Intent.TROUBLESHOOTING;
            case SOLVE -> analysis.intent() == Intent.IMPLEMENTATION;
            case PLAN -> analysis.intent() == Intent.ACTION_PLAN || analysis.intent() == Intent.LEARNING;
            case EXPLAIN, SUMMARIZE, PROVE, GENERAL -> false;
        };
    }

    @Override
    public RuleResult apply(String builtPrompt, String rawInput, AnalysisResult analysis) {
        if (builtPrompt.contains("## Assumptions")) {
            return new RuleResult(builtPrompt, "Assumptions guidance already present.");
        }

        StringBuilder sb = new StringBuilder(builtPrompt);
        if (!builtPrompt.endsWith("\n")) {
            sb.append("\n");
        }

        sb.append("\n## Assumptions\n");
        sb.append("- Do not assume missing requirements without stating them clearly.\n");
        sb.append("- If key details are missing, identify them explicitly.\n");
        sb.append("- Use reasonable defaults only when necessary, and make them explicit.\n");

        return new RuleResult(sb.toString(), "Assumptions section added to reduce unsupported assumptions.");
    }
}

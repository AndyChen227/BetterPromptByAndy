package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;
import com.andy.promptopt.model.Intent;

public class ExamplesRule implements Rule {
    @Override
    public String id() {
        return "R_EXAMPLES";
    }

    @Override
    public String name() {
        return "Add example requirement";
    }

    @Override
    public boolean matches(String rawInput, AnalysisResult analysis) {
        return switch (analysis.taskType()) {
            case EXPLAIN -> analysis.intent() == Intent.CONCEPT || analysis.intent() == Intent.COMPARISON;
            case PLAN -> analysis.intent() == Intent.LEARNING;
            case SOLVE -> analysis.intent() == Intent.IMPLEMENTATION;
            case DEBUG, SUMMARIZE, PROVE, GENERAL -> false;
        };
    }

    @Override
    public RuleResult apply(String builtPrompt, String rawInput, AnalysisResult analysis) {
        if (builtPrompt.contains("## Examples")) {
            return new RuleResult(builtPrompt, "Examples not added because prompt already has an Examples section.");
        }

        StringBuilder sb = new StringBuilder(builtPrompt);
        if (!builtPrompt.endsWith("\n")) {
            sb.append("\n");
        }

        sb.append("\n## Examples\n");
        switch (analysis.domain()) {
            case CODING -> sb.append("- Include a short code example demonstrating usage.\n");
            case LINEAR_ALGEBRA -> sb.append("- Include a small numeric example to illustrate the idea.\n");
            case PHYSICS -> sb.append("- Include a simple physical example or application.\n");
            case WRITING -> sb.append("- Include a brief example to illustrate the point.\n");
            case GENERAL -> sb.append("- Include a simple example to make the answer more concrete.\n");
        }

        return new RuleResult(sb.toString(), "Examples section added for domain-specific guidance.");
    }
}

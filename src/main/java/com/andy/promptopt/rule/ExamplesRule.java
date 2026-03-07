package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;

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
        Domain domain = analysis.domain();
        return domain == Domain.CODING
                || domain == Domain.LINEAR_ALGEBRA
                || domain == Domain.PHYSICS;
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
            case LINEAR_ALGEBRA -> sb.append("- Include a small numerical example illustrating the concept.\n");
            case PHYSICS -> sb.append("- Include a worked example with numbers and units.\n");
            default -> {
                return new RuleResult(builtPrompt, "Examples not added because domain is not supported.");
            }
        }

        return new RuleResult(sb.toString(), "Examples section added for domain-specific guidance.");
    }
}

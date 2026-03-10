package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;

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
        Domain domain = analysis.domain();
        return domain == Domain.CODING
                || domain == Domain.GENERAL
                || domain == Domain.WRITING;
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
        switch (analysis.domain()) {
            case CODING -> {
                sb.append("- Do not assume missing technical requirements without stating them clearly.\n");
                sb.append("- If important details are missing, explicitly identify them before answering.\n");
            }
            case GENERAL -> {
                sb.append("- Do not invent missing context.\n");
                sb.append("- If the request is ambiguous, clearly state the uncertainty.\n");
            }
            case WRITING -> {
                sb.append("- If the audience or format is unclear, state reasonable assumptions before writing.\n");
                sb.append("- Do not assume a tone or structure without explaining it.\n");
            }
            default -> {
                return new RuleResult(builtPrompt, "Assumptions guidance not added because domain is not supported.");
            }
        }

        return new RuleResult(sb.toString(), "Assumptions section added to reduce unsupported assumptions.");
    }
}

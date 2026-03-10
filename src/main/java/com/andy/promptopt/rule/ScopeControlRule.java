package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;

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
        Domain domain = analysis.domain();
        return domain == Domain.GENERAL
                || domain == Domain.WRITING
                || domain == Domain.CODING;
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
        switch (analysis.domain()) {
            case GENERAL, WRITING -> {
                sb.append("- Focus on the user's direct question.\n");
                sb.append("- Do not introduce unrelated subtopics unless necessary.\n");
                sb.append("- Keep the explanation within the scope of the request.\n");
            }
            case CODING -> {
                sb.append("- Focus on solving the specific programming task requested.\n");
                sb.append("- Avoid introducing unrelated frameworks or advanced topics unless necessary.\n");
                sb.append("- Keep the answer aligned with the user's stated problem.\n");
            }
            default -> {
                return new RuleResult(builtPrompt, "Scope guidance not added because domain is not supported.");
            }
        }

        return new RuleResult(sb.toString(), "Scope control guidance added.");
    }
}

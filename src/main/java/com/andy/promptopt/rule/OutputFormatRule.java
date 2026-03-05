package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;

import java.util.ArrayList;
import java.util.List;

public class OutputFormatRule implements Rule {
    @Override
    public String id() {
        return "R_OUTPUT_FORMAT";
    }

    @Override
    public String name() {
        return "Add output format";
    }

    @Override
    public boolean matches(String rawInput, AnalysisResult analysis) {
        return true;
    }

    @Override
    public RuleResult apply(String builtPrompt, String rawInput, AnalysisResult analysis) {
        if (builtPrompt.contains("## Output Format")) {
            return new RuleResult(builtPrompt, "Output format already present.");
        }

        StringBuilder sb = new StringBuilder(builtPrompt);
        if (!builtPrompt.endsWith("\n")) {
            sb.append("\n");
        }
        sb.append("\n## Output Format\n");
        for (String bullet : outputFormatBullets(analysis)) {
            sb.append("- ").append(bullet).append("\n");
        }

        return new RuleResult(sb.toString(), "Output format section added.");
    }

    private List<String> outputFormatBullets(AnalysisResult result) {
        List<String> bullets = new ArrayList<>();
        bullets.add("Use Markdown with bullet points.");
        if (result.domain() == Domain.CODING) {
            bullets.add("Use fenced code blocks for any code.");
        }
        bullets.add("Keep sections separated and labeled.");
        return bullets;
    }
}

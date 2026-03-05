package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;

import java.util.ArrayList;
import java.util.List;

public class ConstraintsRule implements Rule {
    @Override
    public String id() {
        return "R_CONSTRAINTS";
    }

    @Override
    public String name() {
        return "Add constraints";
    }

    @Override
    public boolean matches(String rawInput, AnalysisResult analysis) {
        return true;
    }

    @Override
    public RuleResult apply(String builtPrompt, String rawInput, AnalysisResult analysis) {
        if (builtPrompt.contains("## Constraints")) {
            return new RuleResult(builtPrompt, "Constraints already present.");
        }

        StringBuilder sb = new StringBuilder(builtPrompt);
        if (!builtPrompt.endsWith("\n")) {
            sb.append("\n");
        }
        sb.append("\n## Constraints\n");
        for (String bullet : constraintBullets(analysis)) {
            sb.append("- ").append(bullet).append("\n");
        }

        return new RuleResult(sb.toString(), "Constraints section added.");
    }

    private List<String> constraintBullets(AnalysisResult result) {
        List<String> bullets = new ArrayList<>();
        Domain domain = result.domain();
        switch (domain) {
            case LINEAR_ALGEBRA -> {
                bullets.add("Provide step-by-step reasoning.");
                bullets.add("Define key terms before using them.");
                bullets.add("Include a small example to illustrate the idea.");
                bullets.add("Mention basis or dimension when relevant.");
            }
            case PHYSICS -> {
                bullets.add("State any assumptions explicitly.");
                bullets.add("Use correct units and symbols.");
                bullets.add("Explain the physical meaning of each step.");
                bullets.add("Show steps in a logical sequence.");
            }
            case CODING -> {
                bullets.add("Provide correct, runnable code.");
                bullets.add("Explain the logic briefly.");
                bullets.add("Mention edge cases to consider.");
                bullets.add("Include tests or example usage.");
            }
            case WRITING -> {
                bullets.add("Maintain a clear structure.");
                bullets.add("Use an appropriate tone.");
                bullets.add("Prioritize clarity and concision.");
                bullets.add("Include an outline if it helps.");
            }
            case GENERAL -> {
                bullets.add("Be clear and concise.");
                bullets.add("Ask clarifying questions if needed.");
                bullets.add("Stay focused on the user request.");
            }
        }
        if (bullets.size() > 6) {
            return bullets.subList(0, 6);
        }
        return bullets;
    }
}

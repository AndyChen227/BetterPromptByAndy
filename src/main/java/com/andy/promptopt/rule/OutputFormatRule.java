package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Intent;

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
        return switch (analysis.taskType()) {
            case SOLVE -> analysis.intent() == Intent.IMPLEMENTATION;
            case PLAN -> analysis.intent() == Intent.LEARNING || analysis.intent() == Intent.ACTION_PLAN;
            case EXPLAIN -> analysis.intent() == Intent.COMPARISON;
            case SUMMARIZE -> analysis.intent() == Intent.SUMMARY;
            case DEBUG, PROVE, GENERAL -> false;
        };
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
        Intent intent = result.intent();
        switch (result.taskType()) {
            case EXPLAIN -> {
                if (intent == Intent.CONCEPT) {
                    bullets.add("Use Markdown.");
                    bullets.add("Use bullet points for key ideas.");
                    bullets.add("Use headings for sections.");
                } else if (intent == Intent.COMPARISON) {
                    bullets.add("Use Markdown.");
                    bullets.add("Compare items using bullet points or a table.");
                    bullets.add("Clearly label each item being compared.");
                } else {
                    addDefaultBullets(bullets);
                }
            }
            case SOLVE -> {
                if (intent == Intent.IMPLEMENTATION) {
                    bullets.add("Use Markdown.");
                    bullets.add("Provide code inside fenced code blocks.");
                    bullets.add("Separate explanation and code clearly.");
                } else {
                    addDefaultBullets(bullets);
                }
            }
            case PLAN -> {
                if (intent == Intent.LEARNING || intent == Intent.ACTION_PLAN) {
                    bullets.add("Use Markdown.");
                    bullets.add("Present the response as a numbered list.");
                    bullets.add("Keep each step concise and actionable.");
                } else {
                    addDefaultBullets(bullets);
                }
            }
            case SUMMARIZE -> {
                if (intent == Intent.SUMMARY) {
                    bullets.add("Use Markdown.");
                    bullets.add("Use concise bullet points.");
                    bullets.add("Focus on the most important points first.");
                } else {
                    addDefaultBullets(bullets);
                }
            }
            default -> addDefaultBullets(bullets);
        }
        return bullets;
    }

    private void addDefaultBullets(List<String> bullets) {
        bullets.add("Use Markdown.");
        bullets.add("Use clearly separated sections.");
    }
}

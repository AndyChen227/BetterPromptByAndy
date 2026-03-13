package com.andy.promptopt.build;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;
import com.andy.promptopt.model.Intent;
import com.andy.promptopt.model.TaskType;

public class PromptBuilder {
    public String buildPrompt(AnalysisResult result) {
        AnalysisResult safe = result == null
                ? new AnalysisResult("", 0, 0, Domain.GENERAL, 0.0, TaskType.GENERAL, 0.0,
                Intent.GENERAL, 0.0,
                java.util.List.of(), java.util.Map.of(), java.util.Map.of())
                : result;

        StringBuilder sb = new StringBuilder();

        sb.append("## Context\n");
        sb.append("Domain: ").append(safe.domain())
                .append(" (confidence ")
                .append(formatConfidence(safe.domainConfidence()))
                .append(")\n");
        if (!safe.keywords().isEmpty()) {
            sb.append("Detected keywords: ").append(String.join(", ", safe.keywords())).append("\n");
        }
        sb.append("\n");

        sb.append("## Task\n");
        sb.append(taskSentence(safe)).append("\n");

        return sb.toString();
    }

    private String formatConfidence(double confidence) {
        return String.format(java.util.Locale.US, "%.2f", confidence);
    }

    private String taskSentence(AnalysisResult result) {
        Domain domain = result.domain();
        TaskType taskType = result.taskType();
        Intent intent = result.intent();
        String domainText = switch (domain) {
            case LINEAR_ALGEBRA -> "linear algebra";
            case PHYSICS -> "physics";
            case CODING -> "software";
            case WRITING -> "writing";
            case GENERAL -> "the topic";
        };
        String refined = switch (taskType) {
            case EXPLAIN -> switch (intent) {
                case CONCEPT -> "Explain the concept in " + domainText + " clearly and concisely.";
                case COMPARISON -> "Compare the key ideas in " + domainText + " clearly and concisely.";
                default -> null;
            };
            case SOLVE -> switch (intent) {
                case IMPLEMENTATION -> "Provide a concrete solution or implementation for the task in " + domainText + ".";
                default -> null;
            };
            case DEBUG -> switch (intent) {
                case TROUBLESHOOTING -> "Diagnose the issue in " + domainText + " and suggest a clear fix.";
                default -> null;
            };
            case PLAN -> switch (intent) {
                case LEARNING -> "Provide a step-by-step learning plan for " + domainText + ".";
                case ACTION_PLAN -> "Provide a practical step-by-step plan for the task in " + domainText + ".";
                default -> null;
            };
            case SUMMARIZE -> switch (intent) {
                case SUMMARY -> "Summarize the main points in " + domainText + " succinctly.";
                default -> null;
            };
            default -> null;
        };
        if (refined != null) {
            return refined;
        }
        return switch (taskType) {
            case EXPLAIN -> "Explain the key ideas in " + domainText + " clearly and concisely.";
            case SOLVE -> "Solve the problem in " + domainText + " and present the final result.";
            case PROVE -> "Provide a proof in " + domainText + " with clear logical steps.";
            case DEBUG -> "Diagnose and fix the issue in " + domainText + " with a clear resolution.";
            case SUMMARIZE -> "Summarize the main points in " + domainText + " succinctly.";
            case PLAN -> "Provide a step-by-step plan for addressing the task in " + domainText + ".";
            case GENERAL -> "Address the request clearly, using the provided context.";
        };
    }

}

package com.andy.promptopt;

public class PromptBuilder {
    public String buildPrompt(AnalysisResult result) {
        AnalysisResult safe = result == null
                ? new AnalysisResult("", 0, 0, Domain.GENERAL, 0.0, TaskType.GENERAL, 0.0,
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
        sb.append(taskSentence(safe)).append("\n\n");

        sb.append("## Constraints\n");
        for (String bullet : constraintBullets(safe)) {
            sb.append("- ").append(bullet).append("\n");
        }
        sb.append("\n");

        sb.append("## Output Format\n");
        for (String bullet : outputFormatBullets(safe)) {
            sb.append("- ").append(bullet).append("\n");
        }

        return sb.toString();
    }

    private String formatConfidence(double confidence) {
        return String.format(java.util.Locale.US, "%.2f", confidence);
    }

    private String taskSentence(AnalysisResult result) {
        Domain domain = result.domain();
        TaskType taskType = result.taskType();
        String domainText = switch (domain) {
            case LINEAR_ALGEBRA -> "linear algebra";
            case PHYSICS -> "physics";
            case CODING -> "software";
            case WRITING -> "writing";
            case GENERAL -> "the topic";
        };
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

    private java.util.List<String> constraintBullets(AnalysisResult result) {
        java.util.List<String> bullets = new java.util.ArrayList<>();
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

    private java.util.List<String> outputFormatBullets(AnalysisResult result) {
        java.util.List<String> bullets = new java.util.ArrayList<>();
        bullets.add("Use Markdown with bullet points.");
        if (result.domain() == Domain.CODING) {
            bullets.add("Use fenced code blocks for any code.");
        }
        bullets.add("Keep sections separated and labeled.");
        return bullets;
    }
}

package com.andy.promptopt.evaluator;

import java.util.List;

public class EvaluationResult {
    private final int rawInputLength;
    private final int optimizedPromptLength;
    private final List<String> expectedCapabilities;
    private final int appliedRuleCount;
    private final List<String> appliedRuleIds;
    private final int coveredExpectedCapabilityCount;
    private final int baselineFollowUps;
    private final int optimizedFollowUps;
    private final List<String> notes;

    public EvaluationResult(int rawInputLength,
                            int optimizedPromptLength,
                            List<String> expectedCapabilities,
                            int appliedRuleCount,
                            List<String> appliedRuleIds,
                            int coveredExpectedCapabilityCount,
                            int baselineFollowUps,
                            int optimizedFollowUps,
                            List<String> notes) {
        this.rawInputLength = rawInputLength;
        this.optimizedPromptLength = optimizedPromptLength;
        this.expectedCapabilities = expectedCapabilities == null ? List.of() : List.copyOf(expectedCapabilities);
        this.appliedRuleCount = appliedRuleCount;
        this.appliedRuleIds = appliedRuleIds == null ? List.of() : List.copyOf(appliedRuleIds);
        this.coveredExpectedCapabilityCount = coveredExpectedCapabilityCount;
        this.baselineFollowUps = baselineFollowUps;
        this.optimizedFollowUps = optimizedFollowUps;
        this.notes = notes == null ? List.of() : List.copyOf(notes);
    }

    public int getRawInputLength() {
        return rawInputLength;
    }

    public int getOptimizedPromptLength() {
        return optimizedPromptLength;
    }

    public List<String> getExpectedCapabilities() {
        return expectedCapabilities;
    }

    public int getAppliedRuleCount() {
        return appliedRuleCount;
    }

    public List<String> getAppliedRuleIds() {
        return appliedRuleIds;
    }

    public int getCoveredExpectedCapabilityCount() {
        return coveredExpectedCapabilityCount;
    }

    public int getBaselineFollowUps() {
        return baselineFollowUps;
    }

    public int getOptimizedFollowUps() {
        return optimizedFollowUps;
    }

    public List<String> getNotes() {
        return notes;
    }

    public String toPrettyString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Evaluation ---\n");
        sb.append("Raw input length: ").append(rawInputLength).append("\n");
        sb.append("Optimized prompt length: ").append(optimizedPromptLength).append("\n");
        sb.append("\n");

        sb.append("Expected capabilities for this task: ").append(expectedCapabilities.size()).append("\n");
        for (String capability : expectedCapabilities) {
            sb.append("- ").append(capability).append("\n");
        }
        sb.append("\n");

        sb.append("Applied rules: ").append(appliedRuleCount).append("\n");
        sb.append("Applied rule ids:\n");
        for (String appliedRuleId : appliedRuleIds) {
            sb.append("- ").append(appliedRuleId).append("\n");
        }
        sb.append("\n");

        sb.append("Covered expected capabilities: ")
                .append(coveredExpectedCapabilityCount)
                .append(" / ")
                .append(expectedCapabilities.size())
                .append("\n");
        sb.append("\n");

        sb.append("Baseline follow-ups (estimated): ").append(baselineFollowUps).append("\n");
        sb.append("Optimized follow-ups (estimated): ").append(optimizedFollowUps).append("\n");
        sb.append("\nNotes:\n");
        for (String note : notes) {
            sb.append("- ").append(note).append("\n");
        }
        return sb.toString();
    }
}

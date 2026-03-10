package com.andy.promptopt.evaluator;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.model.Domain;
import com.andy.promptopt.rule.AppliedRule;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PromptEvaluator {
    public EvaluationResult evaluate(
            String rawInput,
            String optimizedPrompt,
            List<AppliedRule> appliedRules,
            AnalysisResult analysis
    ) {
        String safeRawInput = rawInput == null ? "" : rawInput;
        String safeOptimizedPrompt = optimizedPrompt == null ? "" : optimizedPrompt;
        List<AppliedRule> safeAppliedRules = appliedRules == null ? List.of() : appliedRules;

        int rawInputLength = safeRawInput.length();
        int optimizedPromptLength = safeOptimizedPrompt.length();
        int appliedRuleCount = safeAppliedRules.size();
        List<String> appliedRuleIds = new ArrayList<>();
        for (AppliedRule appliedRule : safeAppliedRules) {
            appliedRuleIds.add(appliedRule.id());
        }

        Domain domain = analysis == null ? Domain.GENERAL : analysis.domain();
        Set<String> expectedCapabilities = expectedCapabilities(domain);
        List<String> expectedCapabilitiesList = new ArrayList<>(expectedCapabilities);
        int baselineFollowUps = expectedCapabilities.size();

        Set<String> providedCapabilities = providedCapabilities(safeAppliedRules);
        Set<String> coveredExpectedCapabilities = new LinkedHashSet<>(expectedCapabilities);
        coveredExpectedCapabilities.retainAll(providedCapabilities);
        int coveredExpectedCapabilityCount = coveredExpectedCapabilities.size();
        Set<String> optimizedMissing = new LinkedHashSet<>(expectedCapabilities);
        optimizedMissing.removeAll(providedCapabilities);
        int optimizedFollowUps = optimizedMissing.size();

        List<String> notes = new ArrayList<>();
        notes.add("Expected capabilities for " + domain + ": " + String.join(", ", expectedCapabilities));
        notes.add("Baseline missing capabilities: " + String.join(", ", expectedCapabilities));
        notes.add("Still missing after optimization: "
                + (optimizedMissing.isEmpty() ? "none" : String.join(", ", optimizedMissing)));

        return new EvaluationResult(
                rawInputLength,
                optimizedPromptLength,
                expectedCapabilitiesList,
                appliedRuleCount,
                appliedRuleIds,
                coveredExpectedCapabilityCount,
                baselineFollowUps,
                optimizedFollowUps,
                notes
        );
    }

    private Set<String> expectedCapabilities(Domain domain) {
        Set<String> capabilities = new LinkedHashSet<>();
        switch (domain) {
            case LINEAR_ALGEBRA, PHYSICS -> {
                capabilities.add("example");
                capabilities.add("step-by-step");
            }
            case CODING -> {
                capabilities.add("example");
                capabilities.add("output format");
                capabilities.add("assumption control");
                capabilities.add("scope control");
            }
            case GENERAL -> {
                capabilities.add("clarification");
                capabilities.add("scope control");
                capabilities.add("assumption control");
            }
            case WRITING -> {
                capabilities.add("output format");
                capabilities.add("assumption control");
                capabilities.add("scope control");
            }
        }
        return capabilities;
    }

    private Set<String> providedCapabilities(List<AppliedRule> appliedRules) {
        Set<String> capabilities = new LinkedHashSet<>();
        for (AppliedRule rule : appliedRules) {
            switch (rule.id()) {
                case "R_CLARIFY_QUESTIONS" -> capabilities.add("clarification");
                case "R_EXAMPLES" -> capabilities.add("example");
                case "R_STEP_BY_STEP" -> capabilities.add("step-by-step");
                case "R_OUTPUT_FORMAT" -> capabilities.add("output format");
                case "R_ASSUMPTION_CONTROL" -> capabilities.add("assumption control");
                case "R_SCOPE_CONTROL" -> capabilities.add("scope control");
                default -> {
                }
            }
        }
        return capabilities;
    }
}

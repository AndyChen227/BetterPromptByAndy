package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;

import java.util.ArrayList;
import java.util.List;

public class RulePipeline {
    private final List<Rule> rules;

    public RulePipeline(List<Rule> rules) {
        this.rules = rules;
    }

    public PipelineResult run(String rawInput, String builtPrompt, AnalysisResult analysis) {
        String current = builtPrompt;
        List<AppliedRule> appliedRules = new ArrayList<>();

        for (Rule rule : rules) {
            if (rule.matches(rawInput, analysis)) {
                RuleResult rr = rule.apply(current, rawInput, analysis);
                current = rr.output();
                appliedRules.add(new AppliedRule(rule.id(), rule.name(), rr.reason()));
            }
        }

        return new PipelineResult(current, appliedRules);
    }
}

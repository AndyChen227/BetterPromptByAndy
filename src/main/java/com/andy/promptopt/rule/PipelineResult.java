package com.andy.promptopt.rule;

import java.util.List;

public record PipelineResult(String output, List<AppliedRule> appliedRules) {
}

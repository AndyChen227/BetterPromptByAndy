package com.andy.promptopt.rule;

import com.andy.promptopt.analyze.AnalysisResult;

public interface Rule {
    String id();

    String name();

    boolean matches(String rawInput, AnalysisResult analysis);

    RuleResult apply(String builtPrompt, String rawInput, AnalysisResult analysis);
}

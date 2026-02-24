package com.andy.promptopt;

import java.util.List;
import java.util.Map;

public record AnalysisResult(
        String input,
        int charCount,
        int wordCount,
        Domain domain,
        double domainConfidence,
        TaskType taskType,
        double taskConfidence,
        List<String> keywords,
        Map<Domain, Integer> domainScores,
        Map<TaskType, Integer> taskScores
) {
}

package com.andy.promptopt.analyze;

import com.andy.promptopt.model.Domain;
import com.andy.promptopt.model.Intent;
import com.andy.promptopt.model.TaskType;

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
        Intent intent,
        double intentConfidence,
        List<String> keywords,
        Map<Domain, Integer> domainScores,
        Map<TaskType, Integer> taskScores
) {
}

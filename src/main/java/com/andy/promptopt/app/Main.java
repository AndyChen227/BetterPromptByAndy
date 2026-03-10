package com.andy.promptopt.app;

import com.andy.promptopt.analyze.AnalysisResult;
import com.andy.promptopt.analyze.PromptAnalyzer;
import com.andy.promptopt.build.PromptBuilder;
import com.andy.promptopt.evaluator.EvaluationResult;
import com.andy.promptopt.evaluator.PromptEvaluator;
import com.andy.promptopt.rule.ClarifyQuestionsRule;
import com.andy.promptopt.rule.ConstraintsRule;
import com.andy.promptopt.rule.AssumptionControlRule;
import com.andy.promptopt.rule.ExamplesRule;
import com.andy.promptopt.rule.OutputFormatRule;
import com.andy.promptopt.rule.AppliedRule;
import com.andy.promptopt.rule.PipelineResult;
import com.andy.promptopt.rule.RulePipeline;
import com.andy.promptopt.rule.ScopeControlRule;
import com.andy.promptopt.rule.StepByStepRule;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            String input = extractInput(args);
            if (input != null) {
                runOnce(input);
                return;
            }
            runRepl();
        } catch (Exception e) {
            System.out.printf("Error: %s%n", e.getMessage());
        }
    }

    private static String extractInput(String[] args) {
        if (args == null) {
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            if ("--in".equals(args[i])) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
                return "";
            }
        }
        return null;
    }

    private static void runOnce(String rawInput) {
        PromptAnalyzer analyzer = new PromptAnalyzer();
        PromptBuilder builder = new PromptBuilder();
        AnalysisResult analysis = analyzer.analyze(rawInput);
        String builtPrompt = builder.buildPrompt(analysis);
        RulePipeline pipeline = new RulePipeline(List.of(
                new ClarifyQuestionsRule(),
                new ConstraintsRule(),
                new OutputFormatRule(),
                new ExamplesRule(),
                new StepByStepRule(),
                new AssumptionControlRule(),
                new ScopeControlRule()
        ));
        PipelineResult pipelineResult = pipeline.run(rawInput, builtPrompt, analysis);
        String optimizedPrompt = pipelineResult.output();
        List<AppliedRule> appliedRules = pipelineResult.appliedRules();
        PromptEvaluator evaluator = new PromptEvaluator();
        EvaluationResult evaluationResult = evaluator.evaluate(rawInput, optimizedPrompt, appliedRules, analysis);

        System.out.printf("Domain: %s (confidence %.2f)%n", analysis.domain(), analysis.domainConfidence());
        System.out.print(optimizedPrompt);
        if (!appliedRules.isEmpty()) {
            System.out.print("\n--------------------------------\n");
            System.out.print("Applied Rules:\n");
            for (var appliedRule : appliedRules) {
                System.out.printf(" - %s: %s | %s%n",
                        appliedRule.id(),
                        appliedRule.name(),
                        appliedRule.reason());
            }
        }
        System.out.print("\n--------------------------------\n");
        System.out.print(evaluationResult.toPrettyString());
    }

    private static void runRepl() {
        System.out.print("PromptOpt M2.1\n");
        System.out.print("--------------------------------\n");
        System.out.print("Type your question (or 'exit' to quit):\n");

        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String line = scanner.nextLine();
                if (line == null) {
                    break;
                }
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                if ("exit".equalsIgnoreCase(trimmed)) {
                    System.out.print("Goodbye.\n");
                    break;
                }
                try {
                    System.out.print("Analyzing...\n");
                    System.out.print("--------------------------------\n");
                    runOnce(line);
                    System.out.print("\n--------------------------------\n");
                    System.out.print("Ready for next question.\n");
                } catch (Exception e) {
                    System.out.printf("Error: %s%n", e.getMessage());
                }
            }
        }
    }
}

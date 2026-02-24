package com.andy.promptopt;

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

    private static void runOnce(String input) {
        PromptAnalyzer analyzer = new PromptAnalyzer();
        PromptBuilder builder = new PromptBuilder();
        AnalysisResult result = analyzer.analyze(input);
        System.out.printf("Domain: %s (confidence %.2f)%n", result.domain(), result.domainConfidence());
        System.out.print(builder.buildPrompt(result));
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

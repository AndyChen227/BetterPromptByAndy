package com.andy.promptopt;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String input = extractInput(args);
        if (input == null) {
            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                input = scanner.hasNextLine() ? scanner.nextLine() : "";
            }
        }

        PromptAnalyzer analyzer = new PromptAnalyzer();
        AnalysisResult result = analyzer.analyze(input);
        PromptBuilder builder = new PromptBuilder();
        String markdown = builder.buildPrompt(result);
        System.out.print(markdown);
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
}

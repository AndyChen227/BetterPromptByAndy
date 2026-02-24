package com.andy.promptopt;

public class PromptAnalyzer {
    public AnalysisResult analyze(String input) {
        String safeInput = input == null ? "" : input;
        int charCount = safeInput.length();
        int wordCount = countWords(safeInput);
        String lowered = safeInput.toLowerCase();

        java.util.Map<Domain, Integer> domainScores = scoreDomains(lowered);
        Domain domain = pickDomain(domainScores);
        double domainConfidence = confidence(domainScores, domain);
        if (domain == Domain.GENERAL) {
            domainConfidence = domainScores.values().stream().mapToInt(Integer::intValue).max().orElse(0) == 0 ? 0.0 : domainConfidence;
        }

        java.util.Map<TaskType, Integer> taskScores = scoreTasks(lowered);
        TaskType taskType = pickTaskType(taskScores);
        double taskConfidence = confidence(taskScores, taskType);
        if (taskType == TaskType.GENERAL) {
            taskConfidence = taskScores.values().stream().mapToInt(Integer::intValue).max().orElse(0) == 0 ? 0.0 : taskConfidence;
        }

        java.util.List<String> keywords = extractKeywords(lowered);

        return new AnalysisResult(
                safeInput,
                charCount,
                wordCount,
                domain,
                domainConfidence,
                taskType,
                taskConfidence,
                keywords,
                domainScores,
                taskScores
        );
    }

    private int countWords(String input) {
        int count = 0;
        boolean inWord = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c)) {
                inWord = false;
            } else if (!inWord) {
                count++;
                inWord = true;
            }
        }
        return count;
    }

    private java.util.Map<Domain, Integer> scoreDomains(String input) {
        java.util.Map<Domain, Integer> scores = new java.util.EnumMap<>(Domain.class);
        for (Domain domain : Domain.values()) {
            scores.put(domain, 0);
        }

        java.util.Map<String, Integer> phraseScores = new java.util.LinkedHashMap<>();
        phraseScores.put("null space", 6);
        phraseScores.put("electric field", 5);
        phraseScores.put("magnetic field", 5);

        String remaining = input;
        for (java.util.Map.Entry<String, Integer> entry : phraseScores.entrySet()) {
            String phrase = entry.getKey();
            int weight = entry.getValue();
            int matches = countOccurrences(remaining, phrase);
            if (matches > 0) {
                if ("null space".equals(phrase)) {
                    addScore(scores, Domain.LINEAR_ALGEBRA, weight * matches);
                } else if ("electric field".equals(phrase) || "magnetic field".equals(phrase)) {
                    addScore(scores, Domain.PHYSICS, weight * matches);
                }
                remaining = remaining.replace(phrase, " ");
            }
        }

        String[] tokens = tokenize(remaining);
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            switch (token) {
                case "exception" -> addScore(scores, Domain.CODING, 5);
                case "stacktrace" -> addScore(scores, Domain.CODING, 5);
                case "nullpointerexception" -> addScore(scores, Domain.CODING, 6);
                case "error" -> addScore(scores, Domain.CODING, 4);
                case "bug" -> addScore(scores, Domain.CODING, 4);
                case "compile" -> addScore(scores, Domain.CODING, 3);
                case "runtime" -> addScore(scores, Domain.CODING, 3);
                case "java" -> addScore(scores, Domain.CODING, 2);
                case "python" -> addScore(scores, Domain.CODING, 2);
                case "c++" -> addScore(scores, Domain.CODING, 2);
                case "typescript" -> addScore(scores, Domain.CODING, 2);
                case "hashmap" -> addScore(scores, Domain.CODING, 2);
                case "array" -> addScore(scores, Domain.CODING, 1);
                case "null" -> addScore(scores, Domain.CODING, 1);
                case "kernel" -> addScore(scores, Domain.LINEAR_ALGEBRA, 5);
                case "basis" -> addScore(scores, Domain.LINEAR_ALGEBRA, 4);
                case "rank" -> addScore(scores, Domain.LINEAR_ALGEBRA, 4);
                case "pivot" -> addScore(scores, Domain.LINEAR_ALGEBRA, 3);
                case "eigen" -> addScore(scores, Domain.LINEAR_ALGEBRA, 3);
                case "eigenvalue" -> addScore(scores, Domain.LINEAR_ALGEBRA, 4);
                case "eigenvector" -> addScore(scores, Domain.LINEAR_ALGEBRA, 4);
                case "matrix" -> addScore(scores, Domain.LINEAR_ALGEBRA, 3);
                case "vector" -> addScore(scores, Domain.LINEAR_ALGEBRA, 2);
                case "dimension" -> addScore(scores, Domain.LINEAR_ALGEBRA, 2);
                case "span" -> addScore(scores, Domain.LINEAR_ALGEBRA, 2);
                case "linear" -> addScore(scores, Domain.LINEAR_ALGEBRA, 1);
                case "gauss" -> addScore(scores, Domain.PHYSICS, 5);
                case "ampere" -> addScore(scores, Domain.PHYSICS, 5);
                case "flux" -> addScore(scores, Domain.PHYSICS, 3);
                case "charge" -> addScore(scores, Domain.PHYSICS, 2);
                case "force" -> addScore(scores, Domain.PHYSICS, 3);
                case "energy" -> addScore(scores, Domain.PHYSICS, 2);
                case "newton" -> addScore(scores, Domain.PHYSICS, 2);
                case "unit" -> addScore(scores, Domain.PHYSICS, 2);
                case "voltage" -> addScore(scores, Domain.PHYSICS, 2);
                case "current" -> addScore(scores, Domain.PHYSICS, 2);
                case "essay" -> addScore(scores, Domain.WRITING, 5);
                case "outline" -> addScore(scores, Domain.WRITING, 4);
                case "revise" -> addScore(scores, Domain.WRITING, 4);
                case "rewrite" -> addScore(scores, Domain.WRITING, 4);
                case "thesis" -> addScore(scores, Domain.WRITING, 3);
                case "paragraph" -> addScore(scores, Domain.WRITING, 3);
                case "tone" -> addScore(scores, Domain.WRITING, 2);
                case "clarity" -> addScore(scores, Domain.WRITING, 2);
                case "prompt" -> addScore(scores, Domain.WRITING, 1);
                default -> {
                }
            }
        }

        return scores;
    }

    private java.util.Map<TaskType, Integer> scoreTasks(String input) {
        java.util.Map<TaskType, Integer> scores = new java.util.EnumMap<>(TaskType.class);
        for (TaskType taskType : TaskType.values()) {
            scores.put(taskType, 0);
        }

        java.util.Map<String, Integer> phraseScores = new java.util.LinkedHashMap<>();
        phraseScores.put("show that", 6);
        phraseScores.put("what is", 5);
        phraseScores.put("how to", 5);

        String remaining = input;
        for (java.util.Map.Entry<String, Integer> entry : phraseScores.entrySet()) {
            String phrase = entry.getKey();
            int weight = entry.getValue();
            int matches = countOccurrences(remaining, phrase);
            if (matches > 0) {
                if ("show that".equals(phrase)) {
                    addScore(scores, TaskType.PROVE, weight * matches);
                } else if ("what is".equals(phrase)) {
                    addScore(scores, TaskType.EXPLAIN, weight * matches);
                } else if ("how to".equals(phrase)) {
                    addScore(scores, TaskType.PLAN, weight * matches);
                }
                remaining = remaining.replace(phrase, " ");
            }
        }

        String[] tokens = tokenize(remaining);
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            switch (token) {
                case "exception" -> addScore(scores, TaskType.DEBUG, 6);
                case "stacktrace" -> addScore(scores, TaskType.DEBUG, 6);
                case "nullpointerexception" -> addScore(scores, TaskType.DEBUG, 6);
                case "error" -> addScore(scores, TaskType.DEBUG, 5);
                case "bug" -> addScore(scores, TaskType.DEBUG, 5);
                case "crash" -> addScore(scores, TaskType.DEBUG, 4);
                case "prove" -> addScore(scores, TaskType.PROVE, 6);
                case "derive" -> addScore(scores, TaskType.PROVE, 4);
                case "solve" -> addScore(scores, TaskType.SOLVE, 5);
                case "compute" -> addScore(scores, TaskType.SOLVE, 5);
                case "calculate" -> addScore(scores, TaskType.SOLVE, 5);
                case "find" -> addScore(scores, TaskType.SOLVE, 3);
                case "explain" -> addScore(scores, TaskType.EXPLAIN, 5);
                case "meaning" -> addScore(scores, TaskType.EXPLAIN, 4);
                case "define" -> addScore(scores, TaskType.EXPLAIN, 4);
                case "summarize" -> addScore(scores, TaskType.SUMMARIZE, 6);
                case "tl" -> addScore(scores, TaskType.SUMMARIZE, 0);
                case "overview" -> addScore(scores, TaskType.SUMMARIZE, 3);
                case "plan" -> addScore(scores, TaskType.PLAN, 5);
                case "steps" -> addScore(scores, TaskType.PLAN, 4);
                case "roadmap" -> addScore(scores, TaskType.PLAN, 4);
                default -> {
                }
            }
        }

        if (input.contains("tl;dr")) {
            addScore(scores, TaskType.SUMMARIZE, 6);
        }

        return scores;
    }

    private Domain pickDomain(java.util.Map<Domain, Integer> scores) {
        Domain topDomain = Domain.GENERAL;
        int topScore = 0;
        int secondScore = 0;
        for (java.util.Map.Entry<Domain, Integer> entry : scores.entrySet()) {
            int score = entry.getValue();
            if (score > topScore) {
                secondScore = topScore;
                topScore = score;
                topDomain = entry.getKey();
            } else if (score > secondScore && entry.getKey() != topDomain) {
                secondScore = score;
            }
        }
        double conf = topScore / (topScore + secondScore + 1.0);
        if (topScore == 0 || conf < 0.45) {
            return Domain.GENERAL;
        }
        return topDomain;
    }

    private TaskType pickTaskType(java.util.Map<TaskType, Integer> scores) {
        TaskType topType = TaskType.GENERAL;
        int topScore = 0;
        int secondScore = 0;
        for (java.util.Map.Entry<TaskType, Integer> entry : scores.entrySet()) {
            int score = entry.getValue();
            if (score > topScore) {
                secondScore = topScore;
                topScore = score;
                topType = entry.getKey();
            } else if (score > secondScore && entry.getKey() != topType) {
                secondScore = score;
            }
        }
        double conf = topScore / (topScore + secondScore + 1.0);
        if (topScore == 0 || conf < 0.45) {
            return TaskType.GENERAL;
        }
        return topType;
    }

    private <T extends Enum<T>> double confidence(java.util.Map<T, Integer> scores, T top) {
        int topScore = scores.getOrDefault(top, 0);
        int secondScore = 0;
        for (java.util.Map.Entry<T, Integer> entry : scores.entrySet()) {
            if (entry.getKey() == top) {
                continue;
            }
            secondScore = Math.max(secondScore, entry.getValue());
        }
        return topScore / (topScore + secondScore + 1.0);
    }

    private void addScore(java.util.Map<?, Integer> scores, Enum<?> key, int delta) {
        @SuppressWarnings("unchecked")
        java.util.Map<Enum<?>, Integer> map = (java.util.Map<Enum<?>, Integer>) scores;
        map.put(key, map.getOrDefault(key, 0) + delta);
    }

    private int countOccurrences(String input, String phrase) {
        int count = 0;
        int index = 0;
        while ((index = input.indexOf(phrase, index)) != -1) {
            count++;
            index += phrase.length();
        }
        return count;
    }

    private String[] tokenize(String input) {
        return input.split("[^a-z0-9+]+");
    }

    private java.util.List<String> extractKeywords(String input) {
        java.util.Set<String> stopwords = new java.util.HashSet<>();
        java.util.Collections.addAll(stopwords,
                "the", "and", "for", "with", "that", "this", "what", "how", "are", "you",
                "your", "from", "into", "about", "when", "why", "can"
        );

        java.util.Map<String, Integer> counts = new java.util.HashMap<>();
        for (String token : input.split("[^a-z0-9]+")) {
            if (token.length() < 3) {
                continue;
            }
            if (stopwords.contains(token)) {
                continue;
            }
            counts.put(token, counts.getOrDefault(token, 0) + 1);
        }

        java.util.List<String> tokens = new java.util.ArrayList<>(counts.keySet());
        tokens.sort((a, b) -> {
            int countCompare = Integer.compare(counts.get(b), counts.get(a));
            if (countCompare != 0) {
                return countCompare;
            }
            return a.compareTo(b);
        });

        java.util.List<String> result = new java.util.ArrayList<>();
        for (String token : tokens) {
            result.add(token);
            if (result.size() == 5) {
                break;
            }
        }
        return java.util.Collections.unmodifiableList(result);
    }
}

PromptOpt – BetterPromptByAndy

  A rule-based prompt optimization engine for improving the quality and efficiency of interactions with Large Language Models (LLMs).
  
  PromptOpt analyzes a user's question and automatically restructures it into a structured prompt with domain-aware constraints, output formatting, and reasoning guidance.
  
  The system is designed to reduce the number of follow-up prompts required to complete a task, improving both response quality and interaction efficiency.


Motivation

  Users often ask vague or incomplete questions when interacting with LLMs.
  
  Example:
  
  what is java
  
  This typically leads to incomplete responses such as:
  
  missing examples
  
  inconsistent explanation structure
  
  lack of constraints or context
  
  Users then ask additional follow-up questions:
  
  give example
  simplify it
  show code
  
  This increases:
  
  conversation turns
  
  total token usage
  
  user cognitive load
  
  PromptOpt aims to improve this interaction by automatically optimizing the user's question before it reaches the LLM.


Core Concept

  Instead of sending raw input directly to the model:
  
  User → LLM
  
  PromptOpt introduces an optimization layer:
  
  User Input
       │
       ▼
  Prompt Analyzer
       │
       ▼
  Prompt Builder
       │
       ▼
  Rule Engine
       │
       ▼
  Optimized Prompt
       │
       ▼
      LLM
       │
       ▼
  Response
  
  This architecture allows the system to:
  
  understand the intent of the question
  
  structure prompts consistently
  
  Add domain-specific guidance
  
  enforce output structure
  
  reduce ambiguity


Key Features
  Domain Detection
  
  The system analyzes the user's question to identify the domain and relevant keywords.
  
  Example domains:
  
  Coding
  
  Linear Algebra
  
  Physics
  
  Writing
  
  General Knowledge
  
  Example analysis result:
  
  Domain: CODING
  Confidence: 0.67
  Detected keywords: java
  Structured Prompt Generation
  
  Prompts are automatically organized into structured sections:
  
  ## Context
  ## Task
  ## Constraints
  ## Output Format
  
  This improves response consistency across different questions.
  
  Rule-Based Prompt Optimization
  
  PromptOpt uses a modular rule engine to dynamically enhance prompts.
  
  Rules are applied through a pipeline based on analysis results.
  
  Example rules:
  
  Rule	Purpose
    ClarifyQuestionsRule: Ask clarifying questions for vague inputs
    ConstraintsRule: Add domain-specific constraints
    OutputFormatRule: Enforce structured output
    ExamplesRule (planned): Encourage explanatory examples
    StepByStepRule (planned): Improve reasoning for technical problems
  
  Rules are applied sequentially:
  
  RulePipeline
     ↓
  Rule 1
  Rule 2
  Rule 3
  
  Each rule decides whether it should modify the prompt.


Example

  User input:
  
  what is java
  
  Generated prompt:
  
  ## Context
  Domain: CODING
  
  ## Task
  Explain the key ideas in software clearly and concisely.
  
  ## Constraints
  - Provide correct, runnable code.
  - Explain the logic briefly.
  - Mention edge cases to consider.
  
  ## Output Format
  - Use Markdown with bullet points.
  - Use fenced code blocks for any code.
  
  Applied rules:
  
  R_CONSTRAINTS
  R_OUTPUT_FORMAT



Why Prompt Optimization Matters

  Prompt optimization is not primarily about reducing prompt length.
  
  The real objective is:
  
  Reducing the number of conversation turns needed to complete a task.
  
  Without optimization:
  
  User question
       ↓
  Incomplete answer
       ↓
  Follow-up question
        ↓
  Another follow-up

  
  With optimized prompts:
  
  User question
        ↓
  Structured answer
  
  
  This reduces:
  
  conversation turns
  
  token consumption per task
  
  prompt engineering effort for users



Project Architecture

  promptopt
   ├── analyzer
   │     └── DomainClassifier
   │
   ├── builder
   │     └── PromptBuilder
   │
   ├── rule
   │     ├── ClarifyQuestionsRule
   │     ├── ConstraintsRule
   │     ├── OutputFormatRule
   │
   ├── pipeline
   │     └── RulePipeline
   │
   └── app
         └── Main



Current Version

  Version: v0.2 – Rule-Based Prompt Engine
  
  Current capabilities:
  
  domain analysis
  
  structured prompt generation
  
  rule-based prompt enhancement
  
  command-line interaction



Roadmap

  Phase 1 – Rule Expansion
  
  Add additional prompt optimization rules:
  
  ExamplesRule
  
  StepByStepRule
  
  NoAssumptionsRule
  
  EdgeCaseRule
  
  
  Phase 2 – Prompt Evaluation
  
  Introduce a prompt evaluation module that measures:
  
  prompt token usage
  
  rule effectiveness
  
  estimated interaction cost
  
  optimization efficiency
  
  
  Phase 3 – Tooling and Integration
  
  Potential future extensions:
  
  prompt benchmarking
  
  rule auto-tuning
  
  web interface
  
  integration with LLM APIs



Running the Project

Requirements:

Java 21

Maven

Compile:

mvn compile

Run:

  mvn exec: java
  
  Example CLI session:
  
  PromptOpt M2.1
  --------------------------------
  Type your question (or 'exit' to quit):
  
  > What is Java?



Project Status
  
  PromptOpt is currently an experimental prompt optimization framework intended for:
  
  studying prompt engineering techniques
  
  experimenting with rule-based prompt systems
  
  Exploring prompt efficiency and interaction design

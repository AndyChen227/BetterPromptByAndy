🚀 BetterPromptByAndy

A structured prompt optimization and evaluation tool for LLM users.

📌 Why This Project Exists

AI usage is rapidly increasing. Compared to individuals, companies have much larger and more frequent demand for AI systems.

The goal of this project is to:

Improve the quality of user prompts

Reduce unnecessary token usage

Lower operational AI costs

Provide structured, high-quality query optimization

Make prompt engineering accessible to smaller companies and individuals

While many mature companies already optimize prompts before sending them to LLMs, many small teams and personal projects do not.

This project aims to provide an open-source solution that helps users generate better prompts before they reach the model.

🎯 Core Optimization Goals

1️⃣ User Input Optimization

Transform raw user input into a structured, improved prompt.

The optimized prompt will then be treated by ChatGPT (or other LLMs) as if it were the original user query.

Goal:

Increase clarity

Add missing constraints

Improve response structure

Reduce ambiguity

2️⃣ Prompt Structure Optimization

Prompts are automatically reorganized into four structured sections:

Context – Background information

Task – Explicit objective

Constraints – Domain-specific requirements

Output Format – Structured output guidance

🧠 System Architecture (M2.1)

The current version includes:

Prompt Analyzer

Prompt Builder

Domain Classification

Task Type Classification

CLI Interface (Main)

📊 Domain & Task Classification (M2.1)
Supported Domains

Linear Algebra

Physics

Coding

Writing

General

Supported Task Types

Explain

Solve

Prove

Debug

Summarize

Plan

General

⚙️ How Classification Works

Preprocessing:

Lowercase conversion

Trim spaces

Match longer phrases first

Domain Weighted Scoring:

Each domain has its own keyword library

Matches increase domain score

Top 1–2 domains selected

Confidence score calculated

If confidence < 0.45 → classified as General

Task Type Detection:

Same weighted scoring logic

Determines instruction style

Structured Prompt Generation:

Based on detected domain & task type

Inserts domain-specific constraints

Adds structured output format
package com.andy.promptopt;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MainTest {
    @Test
    void main_usesInArgument() {
        PrintStream originalOut = System.out;
        java.io.InputStream originalIn = System.in;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            Main.main(new String[]{"--in", "Test input"});
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        String output = buffer.toString();
        assertTrue(output.startsWith("Domain: "));
        assertTrue(output.contains("## Context"));
        assertTrue(output.contains("## Task"));
        assertTrue(output.contains("## Constraints"));
        assertTrue(output.contains("## Output Format"));
        assertFalse(output.contains("PromptOpt M2.1"));
        assertFalse(output.contains("Analyzing..."));
        assertFalse(output.contains("Ready for next question."));
        long headingCount = output.lines().filter(line -> line.startsWith("## ")).count();
        assertEquals(4, headingCount);
    }

    @Test
    void repl_exit_printsGoodbye() {
        PrintStream originalOut = System.out;
        java.io.InputStream originalIn = System.in;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        System.setIn(new ByteArrayInputStream("exit\n".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        try {
            Main.main(new String[0]);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        String output = buffer.toString();
        assertTrue(output.contains("PromptOpt M2.1"));
        assertTrue(output.contains("Type your question (or 'exit' to quit):"));
        assertTrue(output.contains("> "));
        assertTrue(output.contains("Goodbye."));
    }
}

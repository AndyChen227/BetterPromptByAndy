package com.andy.promptopt;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
    @Test
    void main_usesInArgument() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
        try {
            Main.main(new String[]{"--in", "Test input"});
        } finally {
            System.setOut(originalOut);
        }
        String output = buffer.toString();
        assertTrue(output.contains("Test input"));
        assertTrue(output.contains("# Prompt Analysis"));
    }
}

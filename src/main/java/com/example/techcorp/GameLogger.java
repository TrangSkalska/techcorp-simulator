package com.example.techcorp;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameLogger {
    private String fileName;

    public GameLogger(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name cannot be blank.");
        }

        this.fileName = fileName;
    }

    public void log(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.out.println("Could not save game log: " + e.getMessage());
        }
    }

    public void clear() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, false))) {
            writer.println("=== TECHCORP SIMULATOR GAME LOG ===");
        } catch (IOException e) {
            System.out.println("Could not clear game log: " + e.getMessage());
        }
    }
}
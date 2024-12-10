package com.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Manages the score for games, allowing scores to be incremented, retrieved, and displayed.
 * This class keeps track of the current score, which updates in real-time as the player
 * progresses, and handles persistent high score tracking.
 */
public class ScoreTracker {
    // Create High Score File
    private static final String FILE_NAME = "high_scores.txt";

    // Write to high_scores.txt
    public static void writeScoreFile(String accountName, String gameName, String gameScore) {
        // Create writer
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(gameName + " " + accountName + " " + gameScore + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read high_score.txt file
    public static List<String[]> readScoreFile() {
        List<String[]> fileScores = new ArrayList();
        // Create reader
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            // Create string for account name, game name, and game score
            String line;
            // Check if line is empty
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    fileScores.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileScores;
    }
}
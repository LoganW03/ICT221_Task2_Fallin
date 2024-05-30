package Fallin.engine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ** Author: Logan Warwick, 1156088 ** //
// Scoring system for GUI version of game. Does not function correctly due to buggy GUI implementation
// Testing recommended via the FunctionalTextGame version of the game. Scoring system works correctly in that version

public class ScoreSystem {
    private static final String SCORE_FILE_PATH = "top_scores.txt";

    public static void calculateScore(int Treasure, int Time) {
        int Score = 20 * Treasure + (100 - Time);
        System.out.println("Score: " + Score);

        List<Integer> topScores = readTopScores();
        updateTopScores(topScores, Score);
        saveTopScores(topScores);
    }

    private static List<Integer> readTopScores() {
        List<Integer> topScores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                topScores.add(Integer.parseInt(line));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading top scores: " + e.getMessage());
        }
        return topScores;
    }

    private static void updateTopScores(List<Integer> topScores, int newScore) {
        topScores.add(newScore);
        Collections.sort(topScores, Collections.reverseOrder());
        if (topScores.size() > 5) {
            topScores.subList(5, topScores.size()).clear();
        }
    }

    private static void saveTopScores(List<Integer> topScores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE_PATH))) {
            for (Integer score : topScores) {
                writer.write(score + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving top scores: " + e.getMessage());
        }
    }
    public static void showHighScores() {
        List<Integer> topScores = readTopScores();
        if (topScores.isEmpty()) {
            System.out.println("No high scores found.");
        } else {
            System.out.println("Top 5 High Scores:");
            for (int i = 0; i < Math.min(5, topScores.size()); i++) {
                System.out.println((i + 1) + ". " + topScores.get(i));
            }
        }
    }
}

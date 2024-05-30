package Fallin.engine;

import Fallin.FunctionalTextGame.GameEngine;

import java.util.Scanner;

// Startup class for text version of game. Functions correctly. Included to allow testing of the game without
// The poorly implemented GUI code

// ** Author: Logan Warwick, 1156088 ** //
public class Main {

    public static void main(String[] args) {
        System.out.println("Cell Designations: 2 (Enemy) | 3 (Treasure) | 4 (Medical Unit) | 5 (Exit) | 8 (Trap)");
        Scanner scanner = new Scanner(System.in);

        int difficulty;
        do {
            System.out.println("Please set the difficulty, with 1 being the easiest, and 10 being the hardest:");
            difficulty = scanner.nextInt();
        } while (difficulty < 1 || difficulty > 10);

        new GameEngine(difficulty); // Pass the difficulty to the GameEngine constructor
    }
}

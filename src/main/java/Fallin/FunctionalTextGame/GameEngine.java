package Fallin.FunctionalTextGame;

import Fallin.engine.ScoreSystem;

import java.util.Random;
import java.util.Scanner;

import static Fallin.FunctionalTextGame.TestCases.*;

// Primary Game Engine for game. Buggy GUI code not included, to allow proper game engine testing
// ** Author: Logan Warwick, 1156088 ** //

public class GameEngine {

    int p1 = 9;
    int p2 = 0;
    int playerLife = 10;
    int difficulty;
    int Time = 0;
    int Treasure = 0;

    public GameEngine(int difficulty) { // Primary constructor
        this.difficulty = difficulty;
        objectSetup(difficulty);
        Controls();
    }


    public static class Map { // Declare 2D array
        public static int[][] Map = new int[10][10];

        public static void main(String[] args) {
        }
    }

    void Controls() { // Handle controls/movement logic
        char Move;
        boolean Running = true;

        Scanner input = new Scanner(System.in);

        Map.Map[p1][p2] = 1; // Place player at the initial position
        Map.Map[0][9] = 5; // Place the victory cell in the top right corner
        print(Map.Map);

        while (Running && Time != 100) {
            System.out.println("Steps taken (Max 100): " + Time + "\nLife remaining: " + playerLife + "\nTreasure collected: " + Treasure);
            System.out.println("Type 'H' for Help");
            Move = input.next().charAt(0);
            int newP1 = p1; // Initialize new
            int newP2 = p2; // position variables
            switch (Character.toUpperCase(Move)) {
                case 'W': // Check for "W"
                    collision(-1, 0); // Move up
                    newP1 = Math.max(0, p1 - 1);
                    Time++; // Decrease Timer by 1 after each move
                    break;
                case 'S': // Check for "S"
                    collision(1, 0); // Move down
                    newP1 = Math.min(Map.Map.length - 1, p1 + 1);
                    Time++; // Decrease Timer by 1 after each move
                    break;
                case 'D': // Check for "D"
                    collision(0, 1); // Move right
                    newP2 = Math.min(Map.Map[0].length - 1, p2 + 1);
                    Time++; // Decrease Timer by 1 after each move
                    break;
                case 'A': // Check for "A"
                    collision(0, -1); // Move left
                    newP2 = Math.max(0, p2 - 1);
                    Time++; // Decrease Timer by 1 after each move
                    break;
                case 'Z': // Check for "Z"
                    ScoreSystem.showHighScores(); // Print recorded highscores
                    break;
                case 'H':
                    System.out.println("Movement controls are WASD.\nUse 'Q' to quit.\nPlayer is given 10 health. Impact with an enemy takes 1 health. Medkits restore 1 health.\nPlayer can collect treasure to increase their score. Player must achieve the highest score possible in the least number of moves.\nDesignations: 2 (Enemy) | 3 (Treasure) | 4 (Medical Unit) | 5 (Exit) | 8 (Trap)");
                    break;
                case 'Q': // Check for "Q"
                    Running = false; // Set "Running" boolean to false
                    input.close();
                    break;
                default:
                    System.out.println("Please Press Proper Keys!");
            }
            Map.Map[p1][p2] = 0; // Set the previous position to 0
            // Update player position
            p1 = newP1;
            p2 = newP2;
            Map.Map[p1][p2] = 1; // Move to the new position
            enemyMovement();
            testEnemyMovement(this);
            print(Map.Map); // Print the map
        }
        ScoreSystem.calculateScore(Treasure, Time); // Calculate score and save highscores
    }

    void collision(int dx, int dy) {
        if (p1 + dx < 0 || p1 + dx >= Map.Map.length || p2 + dy < 0 || p2 + dy >= Map.Map[0].length) { // Check if player is exiting the grid boundary
            System.out.println("Out of bounds!");
            Time--; // Reduce time by 1 (Boundary movement does not count)
            return;
        }

        switch (Map.Map[p1 + dx][p2 + dy]) {
            case 2: // Enemy cell
                System.out.println("Enemy encountered! Player takes damage.");
                playerLife -= 4; // Decrease player's life by 4
                if (playerLife <= 0) {
                    System.out.println("Player has been defeated!");
                    ScoreSystem.calculateScore(Treasure, Time);
                    System.exit(0);
                }
                break;
            case 3: // Treasure cell
                Treasure++;
                System.out.println("Treasure found! Player has collected " + Treasure + " treasure.");
                break;
            case 4: // Medical unit cell
                System.out.println("Medical Unit encountered! Player heals.");
                playerLife += 3; // Increase player's life by 3
                playerLife = Math.min(playerLife, 10); // Cap player's life at maximum of 10
                break;
            case 8: // Trap cell
                System.out.println("Trap encountered! Player takes damage.");
                playerLife -= 2; // Decrease player's life by 2
                if (playerLife <= 0) {
                    System.out.println("Player has been defeated!");
                    ScoreSystem.calculateScore(Treasure, Time);
                    System.exit(0);
                }
                break;
            case 5: // Exit cell
                System.out.println("Congratulations! You have reached the exit!");
                System.out.println("You win!");
                ScoreSystem.calculateScore(Treasure, Time);
                System.exit(0);
                break;
        }
    }


    void objectSetup(int difficulty) {
        Random rand = new Random();
        int enemyCount = 0; // Number of enemies currently spawned
        int treasureCount = 0; // Number of treasure chests currently spawned
        int medicalCount = 0; // Number of medical units currently spawned
        int trapCount = 0; // Number of traps currently spawned

        while (enemyCount != difficulty || treasureCount != 8 || medicalCount != 2 || trapCount != 5) {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10);

            if (Map.Map[x][y] == 0) { // Check if the position is empty
                if (enemyCount != difficulty) { // Check if enemyCount is equal to difficulty value
                    Map.Map[x][y] = 2; // Set enemy position (designated by 2)
                    enemyCount++; // Increment enemyCount by 1
                } else if (treasureCount != 8) { // Check if treasureCount is equal to 8
                    Map.Map[x][y] = 3; // Set treasure chest position (designated by 3)
                    treasureCount++; // Increment treasureCount by 1
                } else if (medicalCount != 2) { // Check if medicalCount is equal to 2
                    Map.Map[x][y] = 4; // Set medical units position (designated by 4)
                    medicalCount++; // Increment medicalCount by 1
                } else {
                    Map.Map[x][y] = 8; // Set trap position (designated by 9)
                    trapCount++; // Increment trapCount by 1
                }
            }
        }
    }

    void enemyMovement() {
        Random rand = new Random();

        for (int i = 0; i < Map.Map.length; i++) {
            for (int j = 0; j < Map.Map[i].length; j++) {
                if (Map.Map[i][j] == 2) { // Check for enemy cell
                    boolean moveUp = rand.nextBoolean();
                    boolean moveDown = rand.nextBoolean();
                    boolean moveLeft = rand.nextBoolean();
                    boolean moveRight = rand.nextBoolean();

                    // Ensure the enemy can only move in one direction
                    if (moveUp && i > 0 && Map.Map[i - 1][j] == 0) {
                        Map.Map[i][j] = 0;
                        Map.Map[i - 1][j] = 2;
                    } else if (moveDown && i < Map.Map.length - 1 && Map.Map[i + 1][j] == 0) {
                        Map.Map[i][j] = 0;
                        Map.Map[i + 1][j] = 2;
                    } else if (moveLeft && j > 0 && Map.Map[i][j - 1] == 0) {
                        Map.Map[i][j] = 0;
                        Map.Map[i][j - 1] = 2;
                    } else if (moveRight && j < Map.Map[i].length - 1 && Map.Map[i][j + 1] == 0) {
                        Map.Map[i][j] = 0;
                        Map.Map[i][j + 1] = 2;
                    }
                }
            }
        }
    }


    private void print(int[][] Map) { // Print grid
        for (int[] row : Map) {
            for (int cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
}
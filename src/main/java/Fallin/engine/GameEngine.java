package Fallin.engine;

import java.util.Random;

// ** Author: Logan Warwick, 1156088 ** //
// GUI version of game engine. Highly buggy. Does not function correctly due to GUI implementation.
// For proper text game/game engine testing, use the version in "FunctionalTextGame"

public class GameEngine {

    int p1 = 9;
    int p2 = 0;
    int playerLife = 10;
    int difficulty;
    int Time = 0;
    int Treasure = 0;
    private static final int MAP_SIZE = 10;

    public GameEngine(int difficulty) {
        this.difficulty = difficulty;
        objectSetup(difficulty);
    }

    public int getPlayerHealth() {
        return playerLife;
    }

    public static class Map { // Declare 2D array
        public static int[][] Map = new int[10][10];
        public static void main(String[] args) {
        }
    }

    public void Controls(char direction) {
        switch (Character.toUpperCase(direction)) {
            case 'W': movePlayer(-1, 0); break;
            case 'S': movePlayer(1, 0); break;
            case 'D': movePlayer(0, 1); break;
            case 'A': movePlayer(0, -1); break;
            default: break;
        }
    }

    public void movePlayer(int dx, int dy) {
        if (isValidMove(dx, dy)) {
            p1 += dx;
            p2 += dy;
            Time++;
            Collision(dx, dy); // Pass the arguments to the collision method
        }
    }

    private boolean isValidMove(int dx, int dy) {
        int newX = p1 + dx;
        int newY = p2 + dy;
        return newX >= 0 && newX < MAP_SIZE && newY >= 0 && newY < MAP_SIZE;
    }

    void Collision(int dx, int dy) {
        if (p1 + dx < 0 || p1 + dx >= Map.Map.length || p2 + dy < 0 || p2 + dy >= Map.Map[0].length) { // Check if player is exiting the grid boundary
            System.out.println("Out of bounds!");
            print(Map.Map);
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
                Treasure++; // Increase treasure value by 1
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

    public void enemyMovement() {
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

    public int[][] getMap() {
        return Map.Map;
    }

    public int getSize() {
        return MAP_SIZE;
    }

    private void print(int[][] Map) { // Print grid
        for (int[] row : Map) {
            for (int cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    public int getPlayerRow() {
        return p1;
    }

    public int getPlayerColumn() {
        return p2;
    }
}
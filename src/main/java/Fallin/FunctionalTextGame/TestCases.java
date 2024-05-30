package Fallin.FunctionalTextGame;

// ** Author: Logan Warwick, 1156088 ** //
// Test cases class. Includes check for enemy movement logic.
// Player movement logic and damage tests included in base game functionality

public class TestCases {
    public static void main(String[] args) {
        // Create a new GameEngine instance
        GameEngine gameEngine = new GameEngine(10); // Adjust difficulty as needed

        // Test enemy movement
        testEnemyMovement(gameEngine);
    }

    public static void testEnemyMovement(GameEngine gameEngine) {
        // Get the initial position of enemy cells
        int[][] initialMapState = copyMap(GameEngine.Map.Map);

        // Execute enemy movement logic
        gameEngine.enemyMovement();

        // Get the position of enemy cells after movement
        int[][] updatedMapState = GameEngine.Map.Map;

        // Check if at least one enemy cell has moved
        boolean enemyMoved = false;
        for (int i = 0; i < initialMapState.length; i++) {
            for (int j = 0; j < initialMapState[i].length; j++) {
                if (initialMapState[i][j] == 2 && updatedMapState[i][j] != 2) {
                    enemyMoved = true;
                    break;
                }
            }
            if (enemyMoved) {
                break;
            }
        }

        // Print the test result
        System.out.println("Enemy Movement Test Result: " + enemyMoved);
    }

    public static int[][] copyMap(int[][] originalMap) {
        int[][] copy = new int[originalMap.length][];
        for (int i = 0; i < originalMap.length; i++) {
            copy[i] = originalMap[i].clone();
        }
        return copy;
    }
}

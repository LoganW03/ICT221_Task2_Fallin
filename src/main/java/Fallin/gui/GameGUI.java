package Fallin.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

// ** Author: Logan Warwick, 1156088 ** //
// GUI of the game. Highly buggy. Collision occurs when player is FACING another cell, instead of on top, due to poor GUI implementation.
// Includes functional health bar, death mechanics, movement, random layout generation, item pickup, timer, and exit.
// Game still functions regardless of the strange offset.
// For proper text game/game engine testing, use the version in "FunctionalTextGame". This version does not have GUI implementation, and works as expected

public class GameGUI extends Application {

    private final Fallin.engine.GameEngine gameEngine = new Fallin.engine.GameEngine(2);
    private final Text[][] cells = new Text[10][10];
    private final ProgressBar healthBar = new ProgressBar();
    private final Text timeLabel = new Text("Time: 0");
    private Timer enemyMoveTimer;
    private int elapsedTime = 0;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Create and add cells to the gridPane
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Text cell = new Text();
                cells[i][j] = cell;
                gridPane.add(cell, j, i); // Note the order of j and i for column and row
            }
        }

        Button upButton = new Button("Up");
        upButton.setOnAction(event -> {
            gameEngine.Controls('W');
            updateMap();
            increaseTimeAndCheckGameOver();
        });

        Button downButton = new Button("Down");
        downButton.setOnAction(event -> {
            gameEngine.Controls('S');
            updateMap();
            increaseTimeAndCheckGameOver();
        });

        Button leftButton = new Button("Left");
        leftButton.setOnAction(event -> {
            gameEngine.Controls('A');
            updateMap();
            increaseTimeAndCheckGameOver();
        });

        Button rightButton = new Button("Right");
        rightButton.setOnAction(event -> {
            gameEngine.Controls('D');
            updateMap();
            increaseTimeAndCheckGameOver();
        });

        Button helpButton = new Button("Help");
        helpButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Help");
            alert.setHeaderText(null);
            alert.setContentText("Movement controls are WASD.\nPlayer is given 10 health. Impact with an enemy takes 1 health. Medkits restore 1 health.\nPlayer can collect treasure to increase their score. Player must achieve the highest score possible in the least number of moves.\nDesignations: 2 (Enemy) | 3 (Treasure) | 4 (Medical Unit) | 5 (Exit) | 8 (Trap)");
            alert.showAndWait();
        });

        HBox buttonBox = new HBox(10, upButton, downButton, leftButton, rightButton, helpButton);
        buttonBox.setAlignment(Pos.CENTER);

        updateMap();

        VBox vbox = new VBox(gridPane, buttonBox, healthBar, timeLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 400, 500);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q) {
                gameEngine.Controls('Q');
                updateMap();
                increaseTimeAndCheckGameOver();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Game GUI");
        primaryStage.show();

        // Start enemy movement timer
        startEnemyMovementTimer();
    }

    private void updateMap() {
        int[][] map = gameEngine.getMap();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == gameEngine.getPlayerRow() && j == gameEngine.getPlayerColumn()) {
                    cells[i][j].setText("P");
                } else if (i == 0 && j == 9) { // Check if it's the exit cell
                    cells[i][j].setText("E");
                } else {
                    cells[i][j].setText(Integer.toString(map[i][j]));
                }
            }
        }
        updateHealthBar();
    }

    private void updateHealthBar() {
        double playerHealth = gameEngine.getPlayerHealth();
        healthBar.setProgress(playerHealth / 10.0);
    }

    private void startEnemyMovementTimer() {
        enemyMoveTimer = new Timer();
        enemyMoveTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                gameEngine.enemyMovement(); // Call enemy movement periodically
                updateMap(); // Update the map after enemy movement
            }
        }, 0, 1000);
    }

    private void increaseTimeAndCheckGameOver() {
        elapsedTime++;
        timeLabel.setText("Time: " + elapsedTime);
        int playerRow = gameEngine.getPlayerRow();
        int playerColumn = gameEngine.getPlayerColumn();
        if (playerRow == 0 && playerColumn == 9) { // Check if the player reaches the exit cell
            endGame();
        }
        if (elapsedTime >= 100) {
            endGame();
        }
    }

    private void endGame() {
        System.out.println("Game Over!");
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.game.Snake;

import com.game.DirectionType;
import com.game.GameManagerUI;
import com.game.ToolBarUI;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SnakeGameBoardTest extends Application {
    private SnakeGameBoard gameBoard;
    private VBox pauseMenu;
    private VBox mainMenu;
    private boolean isFirstStart = true;

    double width = 800;
    double height = 600;

    @Override
    public void start(Stage primaryStage) {
        gameBoard = new SnakeGameBoard(width, height);
        Scene scene = new Scene(gameBoard);

        // Create menus
        mainMenu = createMainMenu();
        pauseMenu = createPauseMenu();
        
        // Initially show main menu and hide pause menu
        mainMenu.setVisible(true);
        pauseMenu.setVisible(false);
        
        // Set menu styles
        String menuStyle = "-fx-background-color: rgba(0, 0, 0, 0.85);";
        mainMenu.setStyle(menuStyle);
        pauseMenu.setStyle(menuStyle);
        
        // Add menus to game board
        gameBoard.getChildren().addAll(mainMenu, pauseMenu);

        scene.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode());

            // If main menu is visible, only handle specific menu keys
            if (mainMenu.isVisible()) {
                return;
            }

            // If pause menu is visible, only handle ESCAPE
            if (pauseMenu.isVisible() && event.getCode() != javafx.scene.input.KeyCode.ESCAPE) {
                return;
            }

            switch (event.getCode()) {
                case ESCAPE:
                    if (gameBoard.getSnakeController().isRunning()) {
                        pauseGame();
                    } else if (pauseMenu.isVisible()) {
                        resumeGame();
                    }
                    break;
                case UP:
                    System.out.println("Up pressed");
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.UP);
                    break;
                case DOWN:
                    System.out.println("Down pressed");
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.DOWN);
                    break;
                case LEFT:
                    System.out.println("Left pressed");
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.LEFT);
                    break;
                case RIGHT:
                    System.out.println("Right pressed");
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.RIGHT);
                    break;
                default:
                    System.out.println("Unhandled key: " + event.getCode());
                    break;
            }
        });

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainMenu() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setPrefSize(600, 400);

        Text titleText = new Text("SNAKE GAME");
        titleText.setFont(new Font("Arial", 48));
        titleText.setFill(Color.GREEN);

        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit Game");

        String buttonStyle = "-fx-background-color: #4CAF50; " +
                           "-fx-text-fill: white; " +
                           "-fx-font-size: 16px; " +
                           "-fx-min-width: 150px; " +
                           "-fx-min-height: 40px;";

        startButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        startButton.setOnAction(e -> startNewGame());
        exitButton.setOnAction(e -> System.exit(0));

        menu.getChildren().addAll(titleText, startButton, exitButton);
        return menu;
    }

    private VBox createPauseMenu() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setPrefSize(600, 400);

        Text pauseText = new Text("PAUSED");
        pauseText.setFont(new Font("Arial", 32));
        pauseText.setFill(Color.WHITE);

        Button resumeButton = new Button("Resume Game");
        Button mainMenuButton = new Button("Main Menu");
        Button exitButton = new Button("Exit Game");

        String buttonStyle = "-fx-background-color: #4CAF50; " +
                           "-fx-text-fill: white; " +
                           "-fx-font-size: 16px; " +
                           "-fx-min-width: 150px; " +
                           "-fx-min-height: 40px;";

        resumeButton.setStyle(buttonStyle);
        mainMenuButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        resumeButton.setOnAction(e -> resumeGame());
        mainMenuButton.setOnAction(e -> returnToMainMenu());
        exitButton.setOnAction(e -> System.exit(0));

        menu.getChildren().addAll(pauseText, resumeButton, mainMenuButton, exitButton);
        return menu;
    }

    private void startNewGame() {
        mainMenu.setVisible(false);
        pauseMenu.setVisible(false);
        gameBoard.getSnakeController().resetGame();
        gameBoard.getSnakeController().startGame();
        gameBoard.requestFocus();
        isFirstStart = false;
    }

    private void pauseGame() {
        gameBoard.getSnakeController().stopGame();
        pauseMenu.setVisible(true);
    }

    private void resumeGame() {
        pauseMenu.setVisible(false);
        // Don't reset the game, just restart the game loop
        if (gameBoard.getSnakeController() != null) {
            gameBoard.getSnakeController().startGame();
            gameBoard.requestFocus();
        }
    }

    private void returnToMainMenu() {
        pauseMenu.setVisible(false);
        mainMenu.setVisible(true);
        gameBoard.getSnakeController().stopGame();
        gameBoard.getSnakeController().resetGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
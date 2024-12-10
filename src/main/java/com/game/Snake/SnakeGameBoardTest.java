package com.game.Snake;

import com.game.DirectionType;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * Entry point for the Snake game. Handles menus, game start, pause, and exit functionality.
 */
public class SnakeGameBoardTest extends Application {
    private SnakeGameBoard gameBoard; // Main game board
    private VBox pauseMenu;           // Pause menu layout
    private VBox mainMenu;            // Main menu layout

    private static final double WIDTH = 800; // Game width
    private static final double HEIGHT = 600; // Game height

    @Override
    public void start(Stage primaryStage) {
        // Initialize the game board
        gameBoard = new SnakeGameBoard(WIDTH, HEIGHT);
        Scene scene = new Scene(gameBoard);

        // Configure main menu callback
        gameBoard.getSnakeController().setOnMainMenuRequest(this::returnToMainMenu);

        // Initialize menus
        mainMenu = createMainMenu();
        pauseMenu = createPauseMenu();

        // Display main menu initially
        mainMenu.setVisible(true);
        pauseMenu.setVisible(false);

        // Apply styles to menus
        String menuStyle = "-fx-background-color: rgba(0, 0, 0, 0.85);";
        mainMenu.setStyle(menuStyle);
        pauseMenu.setStyle(menuStyle);

        // Add menus to the game board
        gameBoard.getChildren().addAll(mainMenu, pauseMenu);

        // Handle key events for game controls
        scene.setOnKeyPressed(event -> {
            if (mainMenu.isVisible()) return; // Ignore keys if main menu is visible
            if (pauseMenu.isVisible() && event.getCode() != javafx.scene.input.KeyCode.ESCAPE) return;

            switch (event.getCode()) {
                case ESCAPE:
                    if (gameBoard.getSnakeController().isRunning()) {
                        pauseGame();
                    } else if (pauseMenu.isVisible()) {
                        resumeGame();
                    }
                    break;
                case UP:
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.UP);
                    break;
                case DOWN:
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.DOWN);
                    break;
                case LEFT:
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.LEFT);
                    break;
                case RIGHT:
                    gameBoard.getSnakeController().updateSnakeDirection(DirectionType.RIGHT);
                    break;
                default:
                    break;
            }
        });

        // Set up the stage
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
     * Creates the main menu layout.
     */
    private VBox createMainMenu() {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.setPrefSize(600, 400);

        Text titleText = new Text("SNAKE GAME");
        titleText.setFont(new Font("Arial", 48));
        titleText.setFill(Color.GREEN);

        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit Game");

        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                "-fx-min-width: 150px; -fx-min-height: 40px;";
        startButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        startButton.setOnAction(e -> startNewGame());
        exitButton.setOnAction(e -> System.exit(0));

        menu.getChildren().addAll(titleText, startButton, exitButton);
        return menu;
    }

    /*
     * Creates the pause menu layout.
     */
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

        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; " +
                "-fx-min-width: 150px; -fx-min-height: 40px;";
        resumeButton.setStyle(buttonStyle);
        mainMenuButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        resumeButton.setOnAction(e -> resumeGame());
        mainMenuButton.setOnAction(e -> returnToMainMenu());
        exitButton.setOnAction(e -> System.exit(0));

        menu.getChildren().addAll(pauseText, resumeButton, mainMenuButton, exitButton);
        return menu;
    }

    /*
     * Starts a new game by resetting the game board.
     */
    private void startNewGame() {
        mainMenu.setVisible(false);
        pauseMenu.setVisible(false);
        gameBoard.getSnakeController().resetGame();
        gameBoard.getSnakeController().startGame();
        gameBoard.requestFocus();
    }

    /*
     * Pauses the game and shows the pause menu.
     */
    private void pauseGame() {
        gameBoard.getSnakeController().stopGame();
        pauseMenu.setVisible(true);
    }

    /*
     * Resumes the game from the pause menu.
     */
    private void resumeGame() {
        pauseMenu.setVisible(false);
        gameBoard.getSnakeController().startGame();
        gameBoard.requestFocus();
    }

    /*
     * Returns to the main menu, resetting the game board.
     */
    private void returnToMainMenu() {
        pauseMenu.setVisible(false);
        mainMenu.setVisible(true);

        GraphicsContext gc = gameBoard.getCanvas().getGraphicsContext2D();
        gc.clearRect(0, 0, gameBoard.getCanvas().getWidth(), gameBoard.getCanvas().getHeight());
        gameBoard.drawBorder(); // Optional: redraw border
        gameBoard.getSnakeController().stopGame();
        gameBoard.getSnakeController().resetGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
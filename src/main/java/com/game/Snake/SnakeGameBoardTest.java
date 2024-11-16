package com.game.Snake;

import com.game.DirectionType;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SnakeGameBoardTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        SnakeGameBoard gameBoard = new SnakeGameBoard(600, 400);

        Scene scene = new Scene(gameBoard);

        // Set up keyboard listeners for the scene
        scene.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode()); // Debug logging
            
            switch (event.getCode()) {
                case R:
                    System.out.println("Resetting game...");
                    gameBoard.getSnakeController().resetGame();
                    gameBoard.getSnakeUI().drawSnake();
                    break;
                case SPACE:
                    System.out.println("Space pressed - Starting game...");
                    gameBoard.getSnakeController().startGame();
                    System.out.println("Game running status: " + gameBoard.getSnakeController().isRunning()); // Debug logging
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
        
        primaryStage.setTitle("SnakeGameBoard Test");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Initial game setup
        System.out.println("Initial game setup complete");
        System.out.println("Press SPACE to start the game!");
    }

    public static void main(String[] args) {
        launch(args);
    }
} 
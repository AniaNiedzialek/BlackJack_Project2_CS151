package com.game.Snake;

import com.game.DirectionType;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SnakeGameBoardTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the SnakeGameBoard for testing
        SnakeGameBoard gameBoard = new SnakeGameBoard(400, 400);

        // Set up buttons
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> gameBoard.startGame());

        // Add components to layout
        BorderPane root = new BorderPane();
        root.setCenter(gameBoard);

        BorderPane buttonPane = new BorderPane();
        buttonPane.setLeft(startButton);
        root.setBottom(buttonPane);

        // Set up the scene
        Scene scene = new Scene(root, 400, 450);

        // Add key event listener to handle direction changes
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> gameBoard.updateDirection(DirectionType.UP);
                case DOWN -> gameBoard.updateDirection(DirectionType.DOWN);
                case LEFT -> gameBoard.updateDirection(DirectionType.LEFT);
                case RIGHT -> gameBoard.updateDirection(DirectionType.RIGHT);
            }
        });

        // Set up the stage
        primaryStage.setTitle("Snake Game Board Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

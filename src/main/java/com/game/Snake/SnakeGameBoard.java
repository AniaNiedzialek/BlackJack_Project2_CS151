package com.game.Snake;

import java.time.LocalTime;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * SnakeGameBoard serves as the main container for the Snake game,
 * managing the canvas and integrating with SnakeUI and SnakeGameController.
 */
public class SnakeGameBoard extends StackPane {
    private Canvas canvas;             // Canvas for rendering
    private SnakeUI snakeUI;           // Handles rendering of game elements
    private SnakeGameController snakeController; // Handles game logic
    private GraphicsContext gc; // Graphics context for drawing

    /**
     * Constructor to set up the Snake game board.
     * 
     * @param width  The width of the board in pixels.
     * @param height The height of the board in pixels.
     */
    public SnakeGameBoard(double width, double height) {
        // Set up the canvas
        canvas = new Canvas(width, height);
        this.getChildren().add(canvas); // Add the canvas to the layout

        // Initialize the graphics context
        gc = canvas.getGraphicsContext2D();

        // Set background dynamically based on the time of day
        LocalTime currentTime = LocalTime.now();
        if (currentTime.isBefore(LocalTime.NOON)) {
            this.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #B2DFDB);");
        } else {
            this.setStyle("-fx-background-color: linear-gradient(to bottom, #0D47A1, #1A237E);");
        }

        // Initialize the UI and controller
        SnakeEntity snake = new SnakeEntity();
        SnakeFoodItem foodItem = new SnakeFoodItem((int) (width / 15), (int) (height / 15));
        snakeUI = new SnakeUI(gc, canvas, snake, foodItem, this);
        if (snakeUI == null) {
            System.out.println("SnakeUI is not initialized correctly.");
        }
        snakeController = new SnakeGameController(this, canvas, (int) (width / 15), (int) (height / 15), snake, foodItem);

        // Listen for resizing events
        this.widthProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
        this.heightProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
    }

    /*
     * Draw aborder around the game area
     */
    public void drawBorder() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Resizes the canvas to match the board dimensions.
     */
    private void resizeCanvas() {
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        drawBorder(); // Redraw the border after resizing
    }

    /**
     * Accessor for the canvas.
     * 
     * @return The game board canvas.
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /*
     * Accessor for SnakeUI
     */
    public SnakeUI getSnakeUI() {
        return snakeUI;
    }

    /*
     * Accessor for SnakeGameController
     */
    public SnakeGameController getSnakeController() {
        return snakeController;
    }
    
}

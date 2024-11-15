package com.game.Snake;

/*
 * Required Imports
 */

import java.util.List;

import com.game.DirectionType;
import com.game.SnakeEntity;
import com.game.SnakeFoodItem;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/*
 * SnakeGameBoard is specificly made for snake game.
 * It contains the dimenstions and limitations of snake movement.
 */
public class SnakeGameBoard extends StackPane {
    private GraphicsContext gc;
    private Canvas canvas;
    private SnakeEntity snake;
    private SnakeFoodItem foodItem;
    private AnimationTimer timer;
    private boolean isRunning = false;

    /*
     * Constructor to set up the Snake game board
     * 
     * @param width  The width of the board in pixels
     * @param height  The heigh of the board in pixes
     */

     public SnakeGameBoard(double width, double height) {
        // Set up the canvas
        canvas = new Canvas(width, height);
        this.getChildren().add(canvas); // Add the canvas to the StackPane layout
        
        // Initialize GraphicsContext
        gc = canvas.getGraphicsContext2D();

        snake = new SnakeEntity(); // Initialize SnakeEntity
        foodItem = new SnakeFoodItem((int) (canvas.getWidth() / 15), (int) (canvas.getHeight() / 15));
        foodItem.spawnFood(snake.getSegmenets()); // Place food initially

        drawBorder(); // Draw the border for the play area
        
        // Set up the animation timer
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) { // Update every 100ms
                    moveSnake();
                    lastUpdate = now;
                }
            }
        };
     }

     public void startGame() {
        if (!isRunning) {
            timer.start(); // Start the animation timer
            isRunning = true;
            this.requestFocus(); // Set focus here, once, when the game starts
        }
     }

     /*
      * Draws a border to define game area
      */
    private void drawBorder() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeRect(0, 0, canvas.getHeight(), canvas.getWidth()); // Draw border
    }

    public void drawSnake() {
        // Clear previous frame
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); 
        drawBorder(); // Redraw border
        
        // Draw food item
        if (foodItem != null) {
            gc.setFill(Color.BURLYWOOD);
            Point2D foodPos = foodItem.getPosition();
            gc.fillOval(foodPos.getX() * 15, foodPos.getY() * 15, 15, 15);
        }

        List<Point2D> segments = snake.getSegmenets();
        for (int i = 0; i < segments.size(); i++) {
            Point2D segment = segments.get(i);
    
            if (i == 0) {
                gc.setFill(Color.DARKRED); // Head color
                gc.fillOval(segment.getX() * 15, segment.getY() * 15, 15, 15);
                gc.setFill(Color.BLACK); // Head eyes
                gc.fillOval(segment.getX() * 15 + 5, segment.getY() * 15 + 5, 3, 3);
            } else {
                gc.setFill(Color.GREEN); // Body color
                gc.fillOval(segment.getX() * 15, segment.getY() * 15, 15, 15);
            }
        }
    }
    
    /*
     * Move the snake and redraws it in the new position
     * Called with animation loop
     */
    public void moveSnake() {
        snake.move(); // Move the snake in its current direction

        // Check for collisions
        if (snake.hasSelfCollision() || snake.hasHitBoundary((int)(canvas.getWidth() / 15), (int)(canvas.getHeight() / 15))) {
            timer.stop();
            System.out.println("Game Over!");
            return;
        }

        if (snake.getHeadPosition().equals(foodItem.getPosition())) {
            snake.grow();
            foodItem.spawnFood(snake.getSegmenets());
        }

        drawSnake(); // Redraw the snake with updated position
        this.requestFocus();
    }     

    public void updateDirection(DirectionType newDirection) {
        snake.setSnakeDirection(newDirection); // Update the direction in SnakeEntity
    }
    
}
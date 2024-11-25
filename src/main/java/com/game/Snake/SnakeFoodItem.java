package com.game.Snake;

import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;

/*
 * Represents a food item in the Snake game, which the snake consumes to grow and score
 * points. This class handles the random positioning of food on the game grid and provides
 * access to the current food location for collision detection with the snake.
 */
public class SnakeFoodItem {
    private Point2D position;
    private Random random = new Random();
    private int gridColumns;
    private int gridRows;

    // Constructor accepting grid dimensions
    public SnakeFoodItem(int gridColumns, int gridRows) {
        this.gridColumns = gridColumns;
        this.gridRows = gridRows;
    }

    public void spawnFood(List<Point2D> snakeSegments) {
        Point2D newPosition;
        do {
            int x = random.nextInt(gridColumns); // Avoid placing food on the border
            int y = random.nextInt(gridRows); // Same here
    
            newPosition = new Point2D(x, y);
        } while (snakeSegments.contains(newPosition)); // Regenerate if position is on the snake
    
        position = newPosition; // Set the position once it's valid
    }
    

    // Getter for position
    public Point2D getPosition() {
        return position;
    }
}
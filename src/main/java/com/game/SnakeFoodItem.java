package com.game;

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
    private int gridWidth;
    private int gridHeight;
    SnakeEntity snake = new SnakeEntity();

    // Constructor accepting grid dimensions
    public SnakeFoodItem(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    public void spawnFood(List<Point2D> snakeSegments) {
        Point2D newPosition;
        do {
            int x = random.nextInt(gridWidth);
            int y = random.nextInt(gridHeight);
            newPosition = new Point2D(x, y);
        } while (snakeSegments.contains(newPosition)); // Regenerate if position is on the snake
        position = newPosition; // Set the position once a valid is found
    }

    // Getter for position
    public Point2D getPosition() {
        return position;
    }
}
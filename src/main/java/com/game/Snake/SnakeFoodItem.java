package com.game.Snake;

import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

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
    private static final int CELL_SIZE = 15; 
    private final GraphicsContext gc;     

    // Constructor accepting grid dimensions
    public SnakeFoodItem(int gridColumns, int gridRows, GraphicsContext gc) {
        this.gridColumns = gridColumns;
        this.gridRows = gridRows;
        this.gc = gc;
    }

    /*
     * Renders the food item with a glowing effect.
     * 
     * @param foodPos Position of the food item
     */
    public void drawFood() {
        if (position == null) return;
        
        double x = position.getX() * CELL_SIZE;
        double y = position.getY() * CELL_SIZE;
        
        // Create glowing gradient effect for food
        RadialGradient gradient = new RadialGradient(
            0, 0, x + CELL_SIZE / 2, y + CELL_SIZE / 2, CELL_SIZE / 2,
            false, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#00FFFF")), // Bright cyan
            new Stop(1, Color.web("#0099FF"))  // Sky blue
        );
        
        gc.setFill(gradient);
        gc.fillOval(x, y, CELL_SIZE, CELL_SIZE);
    }

    public void spawnFood(List<Point2D> snakeSegments) {
        Point2D newPosition;
        int margin = 1; // Margin to avoid the edges of the grid
        do {
            // Generates: 0, 1, 2, 3, 4, 5, 6, 7
            // Then shifts the range to 1, 2, 3, 4, 5, 6, 7, 8
            int x = random.nextInt(gridColumns - 2 * margin) + margin; // Avoid placing food on the border
            int y = random.nextInt(gridRows - 2 * margin) + margin; // Same here
    
            newPosition = new Point2D(x, y);
        } while (snakeSegments.contains(newPosition)); // Regenerate if position is on the snake
    
        position = newPosition; // Set the position once it's valid
    }
    
    // Getter for position
    public Point2D getPosition() {
        return position;
    }
}
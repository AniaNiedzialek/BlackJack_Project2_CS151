package com.game.Snake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.game.DirectionType;

import javafx.geometry.Point2D;

public class SnakeEntity {
    private static final double CELL_SIZE = 15; // Matched with SnakeUI.CELL_SIZE
    private List<Point2D> segments;
    private DirectionType direction;
    private DirectionType lastDirection; // Add this to track the last direction
    private Random random = new Random();
    private SnakeGameBoard gameBoard;
    private SnakeUI snakeUI;
    private int growthPending;
    private final int gridColumns; // Number of columns in the grid
    private final int gridRows;    // Number of rows in the grid
    
    public SnakeEntity(SnakeGameBoard gameBoard, int gridColumns, int gridRows) {
        this.segments = new LinkedList<>();
        this.direction = getRandomDirection(); // Always start moving right
        this.lastDirection = direction;
        this.growthPending = 0;
        this.gameBoard = gameBoard;
        this.gridColumns = gridColumns;
        this.gridRows = gridRows;

    }

        // Setter for SnakeUI
        public void setSnakeUI(SnakeUI snakeUI) {
            this.snakeUI = snakeUI;
        }

    // Method to initialize the snake
    public void initializeSnake() {
        // Calculate the center position
        int maxWidth = (int) (gameBoard.getCanvas().getWidth() / CELL_SIZE);
        int maxHeight = (int) (gameBoard.getCanvas().getHeight() / CELL_SIZE);
        
        // Use grid dimensions
        int centerX = gridColumns / 2; 
        int centerY = gridRows / 2;
        
        segments.clear();
        for (int i = 0; i < 3; i++) {
            segments.add(new Point2D(centerX - i, centerY)); // Horizontal starting position
        }
    }
        
    private DirectionType getRandomDirection() {
        DirectionType[] directions = DirectionType.values();
        return directions[random.nextInt(directions.length)];
    }

    public void move() {
        int[] offset = direction.getMovementOffset();
        // Create a new head position based on current head and direction
        Point2D newHead = segments.get(0).add(offset[0], offset[1]);
        segments.add(0, newHead);
        lastDirection = direction; // Update last direction after successful move

        // Debugging 
        System.out.println("Snake moved to: " + newHead);

        if (growthPending > 0) {
            growthPending--;
        } else {
            segments.remove(segments.size() - 1);
        }
    }

    public void grow() {
        growthPending++; 
    }

    public boolean hasSelfCollision() {
        Point2D head = segments.get(0);
        // Start checking from the fourth segment to allow for tighter turns
        for (int i = 4; i < segments.size(); i++) {
            if (segments.get(i).equals(head)) {
                snakeUI.drawGameOver();
                return true;
            }
        }
        return false;
    }


    // Check if the head of the snake hits the boundaries
    // Subtract 1 from gridColumns and gridRows to account for zero-based indexing
    // This ensures the snake dies exactly when it touches the border
    public boolean hasHitBoundary() {
        Point2D head = segments.get(0);
        // Covert grid coordinates to actual pixels coordinates
        double x = head.getX();
        double y = head.getY();

        // Debug output
        System.out.println("Snake Head Position: (" + x + ", " + y + ")");
        System.out.println("Grid Dimensions: " + gridColumns + "x" + gridRows);

        // Check if the head of the snake git the boundaries
        if (x <= 0 || x >= gridColumns - 1 || y <= 0 || y >= gridRows - 1) {
            snakeUI.drawGameOver();
            return true;
        }
        return false;
    }

    public void setSnakeDirection(DirectionType newDirection) {
        // Prevent 180-degree turns by checking against last direction
        if (!lastDirection.isOpposite(newDirection)) {
            this.direction = newDirection;
            System.out.println("Direction changed to: " + this.direction);
        } else {
            System.out.println("Invalid direction change prevented: " + newDirection);
        }
    }

    public DirectionType getSnakeDirection() {
        return this.direction;
    }

    public void reset() {
        segments.clear();
        direction = DirectionType.RIGHT; // Always start moving right
        lastDirection = direction;
        growthPending = 0;
        initializeSnake();
    }

    public Point2D getHeadPosition() {
        return segments.get(0);
    }

    public List<Point2D> getSegmenets() {
        return new ArrayList<>(segments);
    }
}
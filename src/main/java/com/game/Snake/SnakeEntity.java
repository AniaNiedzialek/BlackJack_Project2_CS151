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
    private int growthPending;
    private Random random = new Random();
    private SnakeGameBoard gameBoard;
    private SnakeUI snakeUI;
    
    public SnakeEntity(SnakeGameBoard gameBoard) {
        this.segments = new LinkedList<>();
        this.direction = getRandomDirection(); // Always start moving right
        this.lastDirection = direction;
        this.growthPending = 0;
        this.gameBoard = gameBoard;
    }

        // Setter for SnakeUI
        public void setSnakeUI(SnakeUI snakeUI) {
            this.snakeUI = snakeUI;
        }

    public void initializeSnake() {
        // Initialize snake with 3 segments, starting from center moving right
        int centerX = 15;  // Start more towards center
        int centerY = 10;  // Start more towards center
        segments.clear();
        for (int i = 0; i < 3; i++) {
            segments.add(new Point2D(centerX - i, centerY));  // Snake starts horizontally
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

    // Check if the snake head hit the boundaries of the game board
    public boolean hasHitBoundary() {
        Point2D head = segments.get(0);
        // Covert grid coordinates to actual pixels coordinates
        double x = head.getX();
        double y = head.getY();

        // Get actual boundaries from the canvas
        double maxWidth = (gameBoard.getCanvas().getWidth() / CELL_SIZE);
        double maxHeight = (gameBoard.getCanvas().getHeight() / CELL_SIZE);

        // Check if the head of the snake git the boundaries
        if (x < 0 || x >= maxWidth || y < 0 || y >= maxHeight) {
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
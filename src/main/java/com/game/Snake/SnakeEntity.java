package com.game.Snake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.game.DirectionType;

import javafx.geometry.Point2D;

/*
 * Represents the snake entity in the game, handling its movement, growth,
 * and collision detection. This class maintains the snake's position,
 * direction, and size.
 */
public class SnakeEntity {
    private static final double CELL_SIZE = 15;
    private List<Point2D> segments;
    private DirectionType direction;
    private DirectionType lastDirection;
    private Random random = new Random();
    private SnakeGameBoard gameBoard;
    private SnakeUI snakeUI;
    private int growthPending;
    private final int gridColumns;
    private final int gridRows;
    private final int maxPossibleLength;
    private boolean hasWon = false;
    
    /*
     * Creates a new snake entity with specified grid dimensions.
     */
    public SnakeEntity(SnakeGameBoard gameBoard, int gridColumns, int gridRows) {
        this.segments = new LinkedList<>();
        this.direction = getRandomDirection();
        this.lastDirection = direction;
        this.growthPending = 0;
        this.gameBoard = gameBoard;
        this.gridColumns = gridColumns;
        this.gridRows = gridRows;
        this.maxPossibleLength = (gridColumns - 2) * (gridRows - 2);
    }

    /*
     * Sets the UI reference for score updates.
     */
    public void setSnakeUI(SnakeUI snakeUI) {
        this.snakeUI = snakeUI;
    }

    /*
     * Initializes the snake in the center of the grid with random direction.
     */
    public void initializeSnake() {
        segments.clear();
        int centerX = gridColumns / 2;
        int centerY = gridRows / 2;
        direction = getRandomDirection();
        lastDirection = direction;

        for (int i = 0; i < 3; i++) {
            Point2D segment = switch (direction) {
                case UP -> new Point2D(centerX, centerY + i);
                case DOWN -> new Point2D(centerX, centerY - i);
                case LEFT -> new Point2D(centerX + i, centerY);
                case RIGHT -> new Point2D(centerX - i, centerY);
            };
            segments.add(segment);
        }
    }

    /*
     * Generates a random direction for the snake.
     */
    private DirectionType getRandomDirection() {
        DirectionType[] directions = DirectionType.values();
        return directions[random.nextInt(directions.length)];
    }

    /*
     * Moves the snake one unit in its current direction.
     */
    public void move() {
        int[] offset = direction.getMovementOffset();
        Point2D newHead = segments.get(0).add(offset[0], offset[1]);
        segments.add(0, newHead);
        lastDirection = direction;

        if (growthPending > 0) {
            growthPending--;
        } else {
            segments.remove(segments.size() - 1);
        }
    }

    /*
     * Increases the snake's length and updates the score.
     */
    public void grow() {
        growthPending++;
        snakeUI.incrementScore();

        // Check if snake has reached maximum possible length
        if (segments.size() + growthPending >= maxPossibleLength) {
            hasWon = true;
        }
    }

    /*
     * Checks if the snake's head collides with its body.
     */
    public boolean hasSelfCollision() {
        Point2D head = segments.get(0);
        for (int i = 4; i < segments.size(); i++) {
            if (segments.get(i).equals(head)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Checks if the snake has hit the game boundaries.
     */
    public boolean hasHitBoundary() {
        Point2D head = segments.get(0);
        double x = head.getX();
        double y = head.getY();
        return x <= 0 || x >= gridColumns - 1 || y <= 0 || y >= gridRows - 1;
    }

    /*
     * Boolean method to check if the plaer has won 
     */
    public boolean hasWon() {
        return hasWon;
    }

    /*
     * Updates the snake's direction unless it would result in a 180-degree turn.
     */
    public void setSnakeDirection(DirectionType newDirection) {
        if (!lastDirection.isOpposite(newDirection)) {
            this.direction = newDirection;
        }
    }

    /*
     * Returns the current direction of the snake.
     */
    public DirectionType getSnakeDirection() {
        return this.direction;
    }

    /*
     * Resets the snake to its initial state.
     */
    public void reset() {
        segments.clear();
        direction = DirectionType.RIGHT;
        lastDirection = direction;
        growthPending = 0;
        snakeUI.resetScore();
        initializeSnake();
    }

    /*
     * Returns the position of the snake's head.
     */
    public Point2D getHeadPosition() {
        return segments.get(0);
    }

    /*
     * Returns a copy of the snake's body segments.
     */
    public List<Point2D> getSegmenets() {
        return new ArrayList<>(segments);
    }
}
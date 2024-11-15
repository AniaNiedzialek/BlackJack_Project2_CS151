package com.game.Snake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.game.DirectionType;

import javafx.geometry.Point2D;

/*
 * Represents the snake in the Snake game, managing its segments, movement, and growth.
 * This class is responsible for advancing the snake in its current direction, extending
 * its body when food is consumed, and detecting self-collisions.
 */
public class SnakeEntity {
    private List<Point2D> segments;
    private DirectionType direction;
    private int growthPending;
    private Random random = new Random();
    
    // Constructor for class SnakeEntity.java
    public SnakeEntity() {
        this.segments = new LinkedList<>();
        this.direction = getRandomDirection();
        this.growthPending = 0;
        initializeSnake(); 
    }

    /*
     * InitializeSNale creates the snake for the game
     */
    private void initializeSnake() {
        // Initialize snake with 3 segments
        for (int i = 0; i < 3; i++) {
            // 1st segment is 5 - 0 = (5, 5) -> HEAD
            // 2nd segment is 5 - 1 = (4, 5) -> BODY
            // 3rd segment is 5 - 2 = (3, 5) -> TAIL
            segments.add(new Point2D(5 - i, 5));
        }
    }

    /*
     * Returns a random initial direction for the snake
     * 
     * @return A random DirectionType (UP, DOWN, LEFT, RIGHT)
     */
    private DirectionType getRandomDirection() {
        DirectionType[] directions = DirectionType.values();
        return directions[random.nextInt(directions.length)];
    }

    /*
     * Advances the snake's position in the current direction.
     * If growthPending is greater than zero, the snake grows by adding
     * a new segment at the head without removing the tail.
     * Otherwise, it maintains its length by removing the tail
     */
    public void move() {
        int[] offset = direction.getMovementOffset();
        
        // offset[0] is the change in x, and offset[1] is the change in y.
        // If the head is at (5,5) and the offset is {1, 0}, 
        // the new head position will be (6,5).
        Point2D newHead = segments.get(0).add(offset[0], offset[1]);

        // Adding the new head position to the sname
        segments.add(0, newHead);

        // Check if the snake is in a growth state (growthPending > 0).
        // If true, decrease growthPending and keep the tail, allowing the snake to grow by one segment.
        // Otherwise, remove the last segment to keep the snake's length constant as it moves.
        if (growthPending > 0) {
            growthPending--; // Decrease growthPending since we handled one "growth" move.
        } else {
            segments.remove(segments.size() - 1); // Remove the tail to maintain constant length.
        }
    }

    /*
     * Increases the snake's pending growth count by one.
     * This method is called when the snake consumes food.
     */
    public void grow() {
        growthPending++; 
    }

    /*
     * Checks if the snake's head has collided with any of its body segments
     * 
     * @return true if there is a self collision, otherwise false.
     */
    public boolean hasSelfCollision() {
        Point2D head = segments.get(0); // The head is at index 0
        for (int i = 1; i < segments.size(); i++) { // Start from i = 1 to skip the head itself
            if (segments.get(i).equals(head)) { // Check if any bodysegment has the same position as the head
                return true; // Collision detected
            }
        }
        return false; // No collision detected
    }

    /*
     * Checks if the snake has hit the boundary of the game grid.
     * 
     * @param gridWidth  The width of the game grid.
     * @param gridHeight  The height of the game frid.
     * @return true if the snake has hit the boundary, otherwise false.
     */
    public boolean hasHitBoundary(int gridWidth, int gridHeight) {
        Point2D head = segments.get(0);
        double x = head.getX();
        double y = head.getY();
        return x < 0 || x >= gridWidth || y < 0 || y >= gridHeight;
    }

    /*
     * Updates the snake's movement direction
     * 
     * @param newDirection  The new directio to set.
     */
    public void setSnakeDirection(DirectionType newDirection) {
        if (!direction.isOpposite(newDirection)) {
            this.direction = newDirection;
            System.out.println("Direction changed to: " + this.direction); // Debugging line
        }
    }

    /*
     * Resets the snake to its initial position and length
     */
    public void reset() {
        segments.clear();
        direction = getRandomDirection();
        growthPending = 0;
        initializeSnake();
    }

    /*
     * Accessor method for the snake's head
     */
    public Point2D getHeadPosition() {
        return segments.get(0);
    }

    /*
     * Returns a list of all segments that make up the snake’s body.
     * It returns a copy of the segments list instead of the original.
     */
    public List<Point2D> getSegmenets() {
        return new ArrayList<>(segments);
    }
}
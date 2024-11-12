package com.cs151.blackjackproject;

/*
 * Enum representing the possible movement directions for the snake in the Snake game.
 * Provides four directions: UP, DOWN, LEFT, and RIGHT, which control the snake's movement.
 */
public enum DirectionType {
    UP, DOWN, LEFT, RIGHT;

    /*
     * Check if the given direction is the opposite of the currect direction
     * 
     * @param other The direction to compare with
     * @return true if the directions are opposites; otherwise, false.
     */
    public boolean isOpposite(DirectionType other) {
        return (this == UP && other == DOWN) || 
               (this == DOWN && other == UP) ||
               (this == RIGHT && other == LEFT) ||
               (this == LEFT && other == RIGHT);
    }

    /*
     * Gets the movement offset for this direction, coordinate change.
     * For example: moving UP means decreasing the y-coordinates by 1
     * 
     * @return An int array where [0] is the x offset and [1] is the y offset.
     */
    public int[] getMovementOffset() {
        switch(this) {
            case UP: 
                return new int[]{0, -1}; // Moves up by decreasing y-axis
            case DOWN: 
                return new int[]{0, 1}; // Moves down by increasing y-axis
            case RIGHT:
                return new int[]{-1, 0}; // Moves right by decreasing x-axis
            case LEFT:
                return new int[]{1, 0}; // Moves left by increasing x-axis
            default:
                return new int[]{0, 0}; // No movement
        }
    }
}
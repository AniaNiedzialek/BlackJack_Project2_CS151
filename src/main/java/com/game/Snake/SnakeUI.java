package com.game.Snake;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;

public class SnakeUI {
    // Constants for cell size and styling
    private static final double CELL_SIZE = 15;
    private static final double SNAKE_WIDTH = CELL_SIZE; // Thickness of the snake
    private double gradientOffset = 0; // Offset for moving gradient

    private final GraphicsContext gc;
    private final Canvas canvas;
    private final SnakeEntity snake;
    private final SnakeFoodItem foodItem;
    private final SnakeGameBoard gameBoard;

    /**
     * Constructor to initialize the SnakeUI with necessary components.
     */
    public SnakeUI(GraphicsContext gc, Canvas canvas, SnakeEntity snake, 
                   SnakeFoodItem foodItem, SnakeGameBoard gameBoard) {
        this.gc = gc;
        this.canvas = canvas;
        this.snake = snake;
        this.foodItem = foodItem;
        this.gameBoard = gameBoard;

        configureGraphicsContext();

        // Set the UI reference back to the snake
        if (snake != null) {
            snake.setSnakeUI(this);
        }
    }

    /**
     * Configures the graphics context with consistent styling.
     */
    private void configureGraphicsContext() {
        if (gc != null) {
            gc.setLineCap(StrokeLineCap.ROUND);
        }
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
    }

    /**
     * Main method to draw the snake, food, and game border on the canvas.
     */
    public void drawSnake() {
        // Clear canvas and draw game boundary
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gameBoard.drawBorder();

        // Draw food if available
        if (foodItem != null && foodItem.getPosition() != null) {
            drawFood(foodItem.getPosition());
        }

        // Draw snake
        List<Point2D> segments = snake.getSegmenets();
        if (!segments.isEmpty()) {
            drawSnakeBody(segments); // Draw connected body with nebula gradient
            drawSnakeHead(segments.get(0)); // Draw distinct head
        }

        // Update gradient offset for next frame to simulate movement
        gradientOffset += 0.01; // Adjust this value for faster or slower movement
        if (gradientOffset > 1) gradientOffset = 0; // Loop the gradient offset
    }

    /**
     * Draws the snake's body with a dynamic nebula-like gradient.
     * 
     * @param segments List of Point2D positions representing the body segments.
     */
    private void drawSnakeBody(List<Point2D> segments) {
        // Static gradient with green shades
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.LIMEGREEN),   // Start color
            new Stop(1, Color.DARKGREEN)   // End color
        );
    
        gc.setStroke(gradient); // Apply gradient as stroke
        gc.setLineWidth(SNAKE_WIDTH); // Thickness of the body
    
        // Draw the connected snake body as a path
        gc.beginPath();
        Point2D start = segments.get(0).multiply(CELL_SIZE);
        gc.moveTo(start.getX() + CELL_SIZE / 2, start.getY() + CELL_SIZE / 2); // Center alignment        
    
        for (int i = 1; i < segments.size(); i++) {
            Point2D segment = segments.get(i).multiply(CELL_SIZE);
            gc.lineTo(segment.getX() + CELL_SIZE / 2, segment.getY() + CELL_SIZE / 2);
        }
    
        gc.stroke();
    }
    
    /**
    * Draws the snake's head with a distinct gradient to match the nebula theme.
     * 
     * @param headPos Position of the snake's head.
     */
    private void drawSnakeHead(Point2D headPos) {
        double x = headPos.getX() * CELL_SIZE + CELL_SIZE / 2;
        double y = headPos.getY() * CELL_SIZE + CELL_SIZE / 2;
    
        // Draw a dark green border (outline) for the head
        gc.setFill(Color.DARKGREEN);
        gc.fillOval(x - SNAKE_WIDTH / 2 - 1, y - SNAKE_WIDTH / 2 - 1, SNAKE_WIDTH + 2, SNAKE_WIDTH + 2);
    
        // Fill the head with lime green
        gc.setFill(Color.LIMEGREEN);
        gc.fillOval(x - SNAKE_WIDTH / 2, y - SNAKE_WIDTH / 2, SNAKE_WIDTH, SNAKE_WIDTH);
    }
        

    /**
     * Draws the food on the canvas.
     * 
     * @param foodPos Position of the food.
     */
    private void drawFood(Point2D foodPos) {
        double x = foodPos.getX() * CELL_SIZE;
        double y = foodPos.getY() * CELL_SIZE;

        // Create a radial gradient for glowing food
        RadialGradient gradient = new RadialGradient(
        0, 0, x + CELL_SIZE / 2, y + CELL_SIZE / 2, CELL_SIZE / 2,
        false, CycleMethod.NO_CYCLE,
        new Stop(0, Color.YELLOW),   // Center color
        new Stop(1, Color.RED)       // Outer color
    );

    gc.setFill(gradient); // Apply gradient as fill
    gc.fillOval(x, y, CELL_SIZE, CELL_SIZE); // Draw the food
}

    /**
     * Displays the "Game Over" message on the canvas.
     */
    public void drawGameOver() {
        // Clear the canvas first
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw game over
        gc.setFill(Color.RED);
        gc.setFont(new Font("Arial Bold", 48)); // Font styling
    
        // Draw glowing effect
        gc.setEffect(new javafx.scene.effect.Glow(0.8));
        gc.fillText("GAME OVER", canvas.getWidth() / 2 - 120, canvas.getHeight() / 2);
    
        // Reset the effect to avoid applying to other drawings
        gc.setEffect(null);
    }
}

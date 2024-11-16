package com.game.Snake;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SnakeUI {

    private GraphicsContext gc;
    private Canvas canvas;
    private SnakeEntity snake;
    private SnakeFoodItem foodItem;
    private SnakeGameBoard gameBoard;

    /*
     * Construcotr for SnakeUI
     */
    public SnakeUI(GraphicsContext gc, Canvas canvas, SnakeEntity snake, 
                    SnakeFoodItem foodItem, SnakeGameBoard gameBoard) {
        this.gc = gc;
        this.canvas = canvas;
        this.snake = snake;
        this.foodItem = foodItem;
        this.gameBoard = gameBoard;
    }

    public void drawSnake() {
        // Clear previous frame
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); 
        gameBoard.drawBorder(); // Redraw border
    
        // Draw food item if position is not null
        if (foodItem != null && foodItem.getPosition() != null) {
            gc.setFill(Color.RED); // Use red color for food
            Point2D foodPos = foodItem.getPosition();
            gc.fillOval(foodPos.getX() * 15, foodPos.getY() * 15, 15, 15);
        }
    
        List<Point2D> segments = snake.getSegmenets();
        for (int i = 0; i < segments.size(); i++) {
            Point2D segment = segments.get(i);
    
            if (i == 0) {
                // Head
                gc.setFill(Color.ORANGE); // Head color
                gc.fillOval(segment.getX() * 15, segment.getY() * 15, 15, 15);
    
                // Eyes
                gc.setFill(Color.WHITE);
                double eyeOffsetX = 5; // Adjust based on snake's direction
                double eyeOffsetY = 3; // Adjust based on snake's direction
                gc.fillOval(segment.getX() * 15 + eyeOffsetX, segment.getY() * 15 + eyeOffsetY, 3, 3);
                gc.fillOval(segment.getX() * 15 + eyeOffsetX + 5, segment.getY() * 15 + eyeOffsetY, 3, 3);
    
                // Tongue
                gc.setStroke(Color.RED);
                gc.setLineWidth(2);
                gc.strokeLine(segment.getX() * 15 + 7.5, segment.getY() * 15 + 7.5, 
                              segment.getX() * 15 + 12, segment.getY() * 15 + 7.5);
            } else {
                // Body
                gc.setFill(Color.GOLDENROD); // Body color
                gc.fillOval(segment.getX() * 15, segment.getY() * 15, 15, 15);
            }
        }
    }    

    public void drawGameOver() {
    gc.setFill(Color.RED);
    gc.setFont(new Font("Arial", 30));
    gc.fillText("Game Over!", canvas.getWidth() / 2 - 75, canvas.getHeight() / 2);
    }

}
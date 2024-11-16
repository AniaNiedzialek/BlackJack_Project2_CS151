package com.game.Snake;

import com.game.DirectionType;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class SnakeGameController {
    private SnakeUI snakeUI;
    private SnakeEntity snake;
    private SnakeFoodItem foodItem;
    private SnakeGameBoard gameBoard;
    private AnimationTimer gameLoop;
    private boolean isRunning = false;
    private Canvas canvas;
    private final int gridWidth;
    private final int gridHeight;

    public SnakeGameController(SnakeGameBoard gameBoard, Canvas canvas, int gridWidth, int gridHeight, SnakeEntity snake, SnakeFoodItem foodItem) {
        this.gameBoard = gameBoard;
        this.canvas = canvas;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.snake = snake;
        this.foodItem = foodItem;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.snakeUI = new SnakeUI(gc, canvas, snake, foodItem, gameBoard);
    }

    public void startGame() {
        // Reset the game state before starting
        snake.reset();
        foodItem.spawnFood(snake.getSegmenets());
        
        if (gameLoop == null) {
            gameLoop = new AnimationTimer() {
                private long lastUpdate = 0;
                private static final long FRAME_INTERVAL = 150_000_000; // Slightly slower for better control
                
                @Override
                public void handle(long now) {
                    if (now - lastUpdate >= FRAME_INTERVAL) {
                        moveSnake();
                        lastUpdate = now;
                    }
                }
            };
            System.out.println("Game loop created.");
        }

        if (!isRunning) {
            isRunning = true;
            gameLoop.start();
            System.out.println("Game loop started.");
            // Initial draw
            snakeUI.drawSnake();
        } else {
            System.out.println("Game loop is already running.");
        }
        
        if (!isRunning) {
            isRunning = true;
            gameLoop.start();
            System.out.println("Game loop started.");
        } else {
            System.out.println("Game loop is already running.");
        }
    }

    public void stopGame() {
        if (!isRunning) {
            System.out.println("Game is already stopped.");
            return;
        }

        if (gameLoop != null) {
            System.out.println("Stopping game loop...");
            gameLoop.stop();
            gameLoop = null;
            System.out.println("Game loop stopped.");
        }

        isRunning = false;
    }    

    public void resetGame() {
        snake.reset();
        foodItem.spawnFood(snake.getSegmenets());
        isRunning = false;
        System.out.println("Game reset. Ready to start again.");
    }
    
    public void updateSnakeDirection(DirectionType newDirection) {
        snake.setSnakeDirection(newDirection);
    }

    public void moveSnake() {
        if (!isRunning) {
            System.out.println("Game is not running. Exiting moveSnake.");
            return;
        }
    
        snake.move();
    
        // Check for collisions
        if (snake.hasSelfCollision() || snake.hasHitBoundary((int)(canvas.getWidth() / 15), (int)(canvas.getHeight() / 15))) {
            System.out.println("Game Over!");
            stopGame();
            snakeUI.drawGameOver();
            return;
        }
    
        if (snake.getHeadPosition().equals(foodItem.getPosition())) {
            snake.grow();
            foodItem.spawnFood(snake.getSegmenets());
        }
    
        snakeUI.drawSnake();
        requestFocus();
    }
        
    public void requestFocus() {
        if (gameBoard != null) {
            gameBoard.requestFocus();
        } else if (canvas != null) {
            canvas.requestFocus();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
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
    private boolean isGameOver = false;

    // Constants for smooth movement
    private static final long MOVE_INTERVAL = 150_000_000; // Time between actual moves (150ms)
    private static final long FRAME_INTERVAL = 16_666_667; // ~60 FPS (16.67ms)

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
        isGameOver = false; // Reset game over state
        if (gameLoop == null) {
            final long[] lastMoveTime = {0};
            final long[] lastRenderTime = {0};
            gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    // Handle movement updates
                    if (!isGameOver) {
                        if (now - lastMoveTime[0] >= MOVE_INTERVAL) {
                            moveSnake();
                            lastMoveTime[0] = now;
                        }
    
                        // Handle rendering at 60 FPS
                        if (now - lastRenderTime[0] >= FRAME_INTERVAL) {
                            snakeUI.drawSnake();
                            lastRenderTime[0] = now;
                        }
                    }
                 }
                    
            };
            System.out.println("Game loop created.");
        }

        if (!isRunning) {
            isRunning = true;
            gameLoop.start();
            System.out.println("Game loop started.");
            snakeUI.drawSnake();
        }
    }

    public void stopGame() {
        if (gameLoop != null) {
            System.out.println("Game is already stopped.");
            gameLoop.stop();
            gameLoop = null;
            System.out.println("Game Loop Stopped");
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
        if (!isRunning || isGameOver) {
            System.out.println("Game is not running. Exiting moveSnake.");
            return;
        }
    
        // Check for collisions BEFORE moving
        if (snake.hasSelfCollision() || snake.hasHitBoundary()) {
            handleGameOver();
            return;
        }

        // Only move if no collision
        snake.move();
    
        // Checks for food collision
        if (snake.getHeadPosition().equals(foodItem.getPosition())) {
            snake.grow();
            foodItem.spawnFood(snake.getSegmenets());
        }
    }

    public void handleGameOver() {
        isGameOver = true;
        isRunning = false;
        stopGame();
        snakeUI.drawGameOver();
        System.out.println("Game Over");
    }

    public boolean isRunning() {
        return isRunning;
    }
}
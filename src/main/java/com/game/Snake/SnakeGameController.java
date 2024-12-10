package com.game.Snake;

import com.game.DirectionType;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/*
 * Controls the game logic, handles user input, and manages the game loop.
 * Coordinates between the snake entity, food, and UI components.
 */
public class SnakeGameController {
    private final SnakeUI snakeUI;
    private final SnakeEntity snake;
    private final SnakeFoodItem foodItem;
    private final SnakeGameBoard gameBoard;
    private AnimationTimer gameLoop;
    private boolean isRunning = false;
    private boolean isGameOver = false;
    private final int gridWidth;
    private final int gridHeight;
    private static final long MOVE_INTERVAL = 700_000_00L;
    private static final long FRAME_INTERVAL = 16_666_667;
    private Runnable onMainMenuRequest;

    /*
     * Initializes the game controller with necessary components.
     */
    public SnakeGameController(SnakeGameBoard gameBoard, Canvas canvas, int gridWidth, int gridHeight, 
                             SnakeEntity snake, SnakeFoodItem foodItem) {
        this.gameBoard = gameBoard;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.snake = snake;
        this.foodItem = foodItem;
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.snakeUI = new SnakeUI(gc, canvas, snake, foodItem, gameBoard);
        Platform.runLater(this::setupButtonHandlers);
    }

    /*
     * Sets up button click handlers for restart and main menu actions.
     */
    private void setupButtonHandlers() {
        snakeUI.getRestartButton().setOnAction(e -> {
            snakeUI.hideGameOverMenu();
            resetGame();
            startGame();
        });

        snakeUI.getMainMenuButton().setOnAction(e -> {
            snakeUI.hideGameOverMenu();
            if (onMainMenuRequest != null) {
                onMainMenuRequest.run();
            }
        });
    }

    public void setOnMainMenuRequest(Runnable callback) {
        this.onMainMenuRequest = callback;
    }

    /*
     * Starts or resumes the game.
     */
    public void startGame() {
        if (gameLoop == null) {
            initializeGameLoop();
        }
        resetGameState();
        gameLoop.start();
    }

    /*
     * Stops the game loop.
     */
    public void stopGame() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        isRunning = false;
    }

    /*
     * Resets the game to initial state.
     */
    public void resetGame() {
        stopGame();
        resetGameState();
        snake.reset();
        foodItem.spawnFood(snake.getSegmenets());
    }

    /*
     * Updates snake direction based on user input if game is active.
     */
    public void updateSnakeDirection(DirectionType newDirection) {
        if (isRunning && !isGameOver) {
            snake.setSnakeDirection(newDirection);
        }
    }

    /*
     * Creates and configures the main game loop.
     */
    private void initializeGameLoop() {
        gameLoop = new AnimationTimer() {
            private long lastMoveTime = 0;
            private long lastRenderTime = 0;

            @Override
            public void handle(long now) {
                if (isGameOver) {
                    snakeUI.drawGameOver();
                    return;
                }

                if (now - lastMoveTime >= MOVE_INTERVAL) {
                    handleGameLogic();
                    lastMoveTime = now;
                }

                if (now - lastRenderTime >= FRAME_INTERVAL) {
                    if (!isGameOver) {
                        snakeUI.drawSnake();
                    }
                    lastRenderTime = now;
                }
            }
        };
    }

    /*
     * Handles game logic including movement, collisions, and food collection.
     */
    private void handleGameLogic() {
        if (!isRunning || isGameOver) {
            return;
        }

        if (snake.hasSelfCollision() || snake.hasHitBoundary()) {
            handleGameOver();
            return;
        }

        snake.move();

        if (snake.hasSelfCollision() || snake.hasHitBoundary()) {
            handleGameOver();
            return;
        }

        if (snake.getHeadPosition().equals(foodItem.getPosition())) {
            snake.grow();
            foodItem.spawnFood(snake.getSegmenets());
        }
    }

    /*
     * Handles game over state.
     */
    private void handleGameOver() {
        isGameOver = true;
        isRunning = false;
        snakeUI.drawGameOver();
    }

    /*
     * Resets game state flags.
     */
    private void resetGameState() {
        isRunning = true;
        isGameOver = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
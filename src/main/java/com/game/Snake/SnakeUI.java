package com.game.Snake;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;

/**
 * The SnakeUI class handles all visual aspects of the Snake game including:
 * - Snake rendering
 * - Food rendering
 * - Game over screen
 * - Menu systems
 * - Score display
 * This class serves as the primary interface between game logic and visual representation.
 */
public class SnakeUI {
    // Constants 
    /** Size of each cell in the game grid */
    private static final double CELL_SIZE = 15;
    /** Width of the snake body */
    private static final double SNAKE_WIDTH = CELL_SIZE;
    /** Used for gradient animations */
    private double gradientOffset = 0;

    // Core Components
    /** Graphics context for drawing on the canvas */
    private final GraphicsContext gc;
    /** Canvas where the game is rendered */
    private final Canvas canvas;
    /** Reference to the snake entity for position data */
    private final SnakeEntity snake;
    /** Reference to food item for position data */
    private final SnakeFoodItem foodItem;
    /** Reference to the main game board */
    private final SnakeGameBoard gameBoard;
    /** Current game score */
    private int score = 0;

    // Menu Components
    /** Container for game over menu elements */
    private final VBox gameOverMenu;
    /** Button to restart the game */
    private final Button restartButton;
    /** Button to return to main menu */
    private final Button mainMenuButton;

    /*
     * Constructs the SnakeUI with all necessary components for game rendering.
     * Initializes the UI elements including buttons and menus.
     *
     * @param gc The graphics context for drawing
     * @param canvas The canvas to draw on
     * @param snake The snake entity
     * @param foodItem The food item
     * @param gameBoard The main game board
     */
    public SnakeUI(GraphicsContext gc, Canvas canvas, SnakeEntity snake, 
                   SnakeFoodItem foodItem, SnakeGameBoard gameBoard) {
        this.gc = gc;
        this.canvas = canvas;
        this.snake = snake;
        this.foodItem = foodItem;
        this.gameBoard = gameBoard;

        // Initialize UI components
        this.restartButton = createStyledButton("Restart Game");
        this.mainMenuButton = createStyledButton("Main Menu");
        
        // Set up game over menu
        this.gameOverMenu = new VBox(20);
        this.gameOverMenu.setAlignment(Pos.CENTER);
        this.gameOverMenu.getChildren().addAll(restartButton, mainMenuButton);
        this.gameOverMenu.setVisible(false);

        configureGraphicsContext();
        if (snake != null) {
            snake.setSnakeUI(this);
        }
    }

    /*
     * Creates a styled button with consistent appearance and hover effects.
     * 
     * @param text The button label
     * @return A styled Button instance
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        
        // Define base button style
        String buttonStyle = "-fx-background-color: #4CAF50; " +
                           "-fx-text-fill: white; " +
                           "-fx-font-size: 18px; " +
                           "-fx-padding: 15 50; " +
                           "-fx-background-radius: 5; " +
                           "-fx-cursor: hand; " +
                           "-fx-min-width: 200px;";
        
        // Define hover effect style
        String hoverStyle = "-fx-background-color: #5DBE60; " +
                          "-fx-text-fill: white; " +
                          "-fx-font-size: 18px; " +
                          "-fx-padding: 15 50; " +
                          "-fx-background-radius: 5; " +
                          "-fx-cursor: hand; " +
                          "-fx-min-width: 200px;";
        
        // Apply styles and hover effects
        button.setStyle(buttonStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(buttonStyle));
        
        return button;
    }

    /*
     * Displays the game over screen with final score and menu options.
     * Includes a semi-transparent overlay on the final game state.
     */
    public void drawGameOver() {
        // Show final game state
        drawSnake();
        
        // Add darkening overlay
        gc.setFill(new Color(0, 0, 0, 0.65));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw game over text
        gc.setFill(Color.RED);
        gc.setFont(new Font("Arial Bold", 48));
        gc.setEffect(new Glow(0.8));
        gc.fillText("GAME OVER", canvas.getWidth() / 2 - 150, canvas.getHeight() / 4);
        
        // Draw final score
        gc.setFill(Color.LIMEGREEN);
        gc.setFont(new Font("Arial", 36));
        gc.setEffect(new Glow(0.6));
        gc.fillText("Score: " + score, canvas.getWidth() / 2 - 70, canvas.getHeight() / 2.8);
        gc.setEffect(null);
    
        // Position and show menu
        gameOverMenu.setLayoutX((canvas.getWidth() - 200) / 2);
        gameOverMenu.setLayoutY(canvas.getHeight() / 1.5);
        gameOverMenu.setVisible(true);
        
        if (!gameBoard.getChildren().contains(gameOverMenu)) {
            gameBoard.getChildren().add(gameOverMenu);
        }
    }

    /*
     * Main render method for the game state.
     * Draws the snake, food, and border in their current positions.
     */
    public void drawSnake() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gameBoard.drawBorder();

        // Draw food if it exists
        if (foodItem != null && foodItem.getPosition() != null) {
            drawFood(foodItem.getPosition());
        }

        // Draw snake if it exists
        List<Point2D> segments = snake.getSegmenets();
        if (!segments.isEmpty()) {
            drawSnakeBody(segments);
            drawSnakeHead(segments.get(0));
        }

        // Update gradient animation
        gradientOffset = (gradientOffset + 0.01) % 1;
    }

    /*
     * Renders the snake's body with a gradient effect.
     * 
     * @param segments List of points representing snake segments
     */
    private void drawSnakeBody(List<Point2D> segments) {
        // Create gradient effect for snake body
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.LIMEGREEN),
            new Stop(1, Color.DARKGREEN)
        );
        
        gc.setStroke(gradient);
        gc.setLineWidth(SNAKE_WIDTH);
        
        // Draw connected snake segments
        gc.beginPath();
        Point2D start = segments.get(0).multiply(CELL_SIZE);
        gc.moveTo(start.getX() + CELL_SIZE / 2, start.getY() + CELL_SIZE / 2);
        
        for (int i = 1; i < segments.size(); i++) {
            Point2D segment = segments.get(i).multiply(CELL_SIZE);
            gc.lineTo(segment.getX() + CELL_SIZE / 2, segment.getY() + CELL_SIZE / 2);
        }
        
        gc.stroke();
    }

    /*
     * Renders the snake's head with a distinct appearance.
     * 
     * @param headPos Position of the snake's head
     */
    private void drawSnakeHead(Point2D headPos) {
        double x = headPos.getX() * CELL_SIZE + CELL_SIZE / 2;
        double y = headPos.getY() * CELL_SIZE + CELL_SIZE / 2;
        
        // Draw head outline
        gc.setFill(Color.DARKGREEN);
        gc.fillOval(x - SNAKE_WIDTH / 2 - 1, y - SNAKE_WIDTH / 2 - 1, SNAKE_WIDTH + 2, SNAKE_WIDTH + 2);
        
        // Draw head interior
        gc.setFill(Color.LIMEGREEN);
        gc.fillOval(x - SNAKE_WIDTH / 2, y - SNAKE_WIDTH / 2, SNAKE_WIDTH, SNAKE_WIDTH);
    }

    /*
     * Renders the food item with a glowing effect.
     * 
     * @param foodPos Position of the food item
     */
    private void drawFood(Point2D foodPos) {
        double x = foodPos.getX() * CELL_SIZE;
        double y = foodPos.getY() * CELL_SIZE;
        
        // Create glowing gradient effect for food
        RadialGradient gradient = new RadialGradient(
            0, 0, x + CELL_SIZE / 2, y + CELL_SIZE / 2, CELL_SIZE / 2,
            false, CycleMethod.NO_CYCLE,
            new Stop(0, Color.YELLOW),
            new Stop(1, Color.RED)
        );
        
        gc.setFill(gradient);
        gc.fillOval(x, y, CELL_SIZE, CELL_SIZE);
    }

    /*
     * Configures the graphics context for smooth rendering.
     */
    private void configureGraphicsContext() {
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
    }

    // Utility Methods

    /**
     * Hides and removes the game over menu from display.
     */
    public void hideGameOverMenu() {
        gameOverMenu.setVisible(false);
        gameBoard.getChildren().remove(gameOverMenu);
    }

    /*
     * Increments the game score.
     */
    public void incrementScore() {
        score++;
    }

    /*
     * Resets the game score to zero.
     */
    public void resetScore() {
        score = 0;
    }

    /*
     * @return The restart game button
     */
    public Button getRestartButton() {
        return restartButton;
    }

    /*
     * @return The main menu button
     */
    public Button getMainMenuButton() {
        return mainMenuButton;
    }
}
package com.game.Snake;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/*
 * SnakeGameBoard serves as the main container for the Snake game,
 * managing the canvas and integrating with SnakeUI and SnakeGameController.
 */
public class SnakeGameBoard extends StackPane {
    private final int gridColumns; // Number of columns in the grid
    private final int gridRows;    // Number of rows in the grid
    private static final int CELL_SIZE = 15; // Grid cell size
    private Canvas canvas;                   // Canvas for rendering
    private SnakeUI snakeUI;                 // Handles rendering of game elements
    private SnakeGameController snakeController; // Handles game logic
    private GraphicsContext gc;             // Graphics context for drawing

    // Define the background style in a shared location (like a constant or CSS file)
    String backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #002b00, #004d00);";

    /*
     * Constructor to set up the Snake game board.
     * 
     * @param width  The width of the board in pixels.
     * @param height The height of the board in pixels.
     */
    public SnakeGameBoard(double width, double height) {
        
        // Canvas dimenstions are divisble by the cell size
        double adjustedWidth = Math.floor(width / CELL_SIZE) * CELL_SIZE;
        double adjustedHeight = Math.floor(height / CELL_SIZE) * CELL_SIZE;

        canvas = new Canvas(adjustedWidth, adjustedHeight);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);
    
        // Unified background style for both game and menus
        String backgroundStyle = "-fx-background-color: linear-gradient(to bottom, #121212, #0A0A0A);";
        this.setStyle(backgroundStyle); // Apply the unified style

        // Calculate grid dimensions
        gridColumns = (int) (adjustedWidth / CELL_SIZE);
        gridRows = (int) (adjustedHeight / CELL_SIZE);
        
        // Create game entities
        SnakeEntity snake = new SnakeEntity(this, gridColumns, gridRows);
        SnakeFoodItem foodItem = new SnakeFoodItem(gridColumns, gridRows);

        // Create UI and controller with proper references
        snakeUI = new SnakeUI(gc, canvas, snake, foodItem, this);
        snakeController = new SnakeGameController(this, canvas, (int) (width / 15), (int) (height / 15), snake, foodItem);
    
        this.widthProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
        this.heightProperty().addListener((obs, oldVal, newVal) -> resizeCanvas());
    }
    
    /*
     * Draw aborder around the game area
     */
    public void drawBorder() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /*
     * Resizes the canvas to match the board dimensions.
     */
    private void resizeCanvas() {
        double newWidth = Math.floor(getWidth() / CELL_SIZE) * CELL_SIZE;
        double newHeight = Math.floor(getHeight() / CELL_SIZE) * CELL_SIZE;
    
        canvas.setWidth(newWidth);
        canvas.setHeight(newHeight);
    
        // Redraw the border
        drawBorder();
    }
    

    /*
     * Accessor for the canvas.
     * 
     * @return The game board canvas.
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /*
     * Accessor for SnakeUI
     */
    public SnakeUI getSnakeUI() {
        return snakeUI;
    }

    /*
     * Accessor for SnakeGameController
     */
    public SnakeGameController getSnakeController() {
        return snakeController;
    }
    
}

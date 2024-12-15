package com.game.Snake;

import com.game.SessionManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/*
 * Manage score-related functionality including viewing and score tracking
 * Displays score in the border area of the game where snake cannot move
 */
public class ScoreManager {
    // Constants
    private static final int CELL_SIZE = 15; // Typical game's cell size for border
    private static final double GLOW_INTENSITY = 0.3; // Glow effect

    // Score tracking
    private int currentScore;
    private int highScore;

    // Canvas references
    private final GraphicsContext gc;
    private final double canvasWidth;
    private final double canvasHeight;

    /*
     * Constructor for the class
     */
    public ScoreManager(GraphicsContext gc, double canvasWidth, double canvasHeight) {
        this.gc = gc;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.currentScore = 0;
        this.highScore = 0;
    }

    /*
     * Draw the score in the top-right border area
     */
    public void drawScore() {
        // Calculate position in top-right border area
        String scoreText = String.format("Score: %d", currentScore);
        double textWidth = scoreText.length() * 8; // Approximate width per character
        
        // Adjust x position based on text width to keep right-aligned
        double x = canvasWidth - textWidth - (CELL_SIZE * 2); // Reduced from 8 to 2 cells padding
        double y = CELL_SIZE * 0.8;
        
        gc.save();
        
        // Text style
        gc.setFill(Color.web("#98FF98"));
        gc.setFont(Font.font("Monospace", FontWeight.BOLD, 14));
        
        // Glow effect
        Glow glow = new Glow(GLOW_INTENSITY);
        gc.setEffect(glow);
        
        // Draw score right-aligned
        gc.fillText(scoreText, x, y);
        
        gc.setEffect(null);
        gc.restore();
    }

    /*
     * Draws the final score screen (game over or win)
     */
    public void drawFinalScore(String headerText, Color headeColor) {
        // Darkend background overlay
        gc.setFill(new Color(0, 0, 0, 0.65));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // Draw header text (GAME OVER ir YOU WIN)
        gc.setFill(headeColor);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setEffect(new Glow(0.8));

        // Center the header text
        double textWidth = headerText.length() * 25; // Approximate width
        gc.fillText(headerText, canvasWidth / 2 - textWidth / 2, canvasHeight / 4);

        // Draw final score
        gc.setFill(Color.LIMEGREEN);
        gc.setFont(Font.font("Arial", 36));
        gc.setEffect(new Glow(0.6));
        String scoreText = "Final Score: " + currentScore;
        gc.fillText(scoreText, canvasWidth / 2 - 70, canvasHeight / 2.8);

        // Reset effects
        gc.setEffect(null);
    }

    /*
     * Increment score by 1000 for each food eater
     */
    public void incrementScore() {
        currentScore += 1000;
        if (currentScore > highScore) {
            highScore = currentScore;
            SessionManager.getInstance().setCurrentScore(String.valueOf(highScore));
        }
    }

    /*
     * Reset score to zero
     */
    public void resetScore() {
        currentScore = 0;
    }

    /*
     * Get current score
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /*
     * Get high score
     */
    public int getHighScore() {
        return highScore;
    }
}
package com.game;

/*
 * GameControler interface contains core game functionalities that
 * both Snake and BlackJack games require.
 * Contains methods for starting, ending, and pausing the game.
 */
public interface GameController {
    void startGame(); // Starts the game
    void endGame(); // Ends the game
    void pauseGame(); // Pause the game
}
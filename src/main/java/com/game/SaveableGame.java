package com.game;

/*
 * SaveAble game interface contains methods foer games that need
 * to save and load game states.
 * The interface is implemented by BackJack
 */
public interface SaveableGame {
    void saveGameState(); // Save the curret game state, allows user to resume from the saved spot
    void loadGameState(); // Load previously saved game state.
}
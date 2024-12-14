package com.game.BlackJack;

public class AIPlayer extends Player {
    public AIPlayer(String name, int balance) {
        super(name, balance);
    }

    @Override
    public void takeTurn(GameController gameController) {
        while (calculateHandValue() < 16) {
            addCard(gameController.getDeck().dealCard());
        }
    }
}
package com.game.BlackJack;

public class Dealer extends Player {
    public Dealer() {
        super("Dealer", 0);
    }

    @Override
    public void takeTurn(BlackjackGameController gameController) {
        while (calculateHandValue() < 17) {
            addCard(gameController.getDeck().dealCard());
        }
    }
}
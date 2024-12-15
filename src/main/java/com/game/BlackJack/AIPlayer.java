package com.game.BlackJack;

public class AIPlayer extends Player {
    public AIPlayer(String name, int balance) {
        super(name, balance);
    }

    @Override
    public void takeTurn(BlackjackGameController gameController) {
        if (isBusted()) return;
    
        int dealerUpCardValue = gameController.getDealer().getHand().get(0).getValue();
        while (calculateHandValue() < 16 || (calculateHandValue() < 18 && dealerUpCardValue >= 7)) {
            addCard(gameController.getDeck().dealCard());
            System.out.println(getName() + " hits.");
            if (isBusted()) return;
        }
    
        System.out.println(getName() + " stands.");
    }      
}
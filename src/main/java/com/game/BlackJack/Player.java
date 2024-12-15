package com.game.BlackJack;

import java.util.ArrayList;

public abstract class Player {
    private String name;
    private ArrayList<Card> hand;
    private int balance;
    private int bet;

    public Player(String name, int initialBalance) {
        this.name = name;
        this.balance = initialBalance;
        this.hand = new ArrayList<>();
        this.bet = 0;
    }

    // Methods for betting
    public void placeBet(int betAmount) {
        if (this instanceof AIPlayer) {
            this.bet = betAmount; // Directly set the bet for AI players without balance checks
        } else {
            if (betAmount <= balance) {
                this.bet = betAmount;
                this.balance -= betAmount;
            } else {
                throw new IllegalArgumentException(name + " has insufficient balance to place this bet!");
            }
        }
    }
    

    public void winBet() {
        this.balance += this.bet * 2; // Winner gets 2x their bet
        this.bet = 0; // Reset bet after winning
    }

    public void loseBet() {
        this.bet = 0; // Reset bet after losing
    }

    public void tieBet() {
        this.balance += this.bet; // Return the bet on a tie
        this.bet = 0; // Reset bet after tie
    }

    // Methods for managing the player's hand
    public void addCard(Card card) {
        hand.add(card);
    }

    public void clearHand() {
        hand.clear();
    }

    public int calculateHandValue() {
        int value = 0;
        int aces = 0;

        for (Card card : hand) {
            value += card.getValue();
            if (card.getRank().equals("Ace")) {
                aces++;
            }
        }

        // Adjust for aces (Ace = 1 if value > 21)
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public boolean isBusted() {
        return calculateHandValue() > 21;
    }

    // Getters and setters
    public ArrayList<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void adjustBalance(int amount) {
        balance += amount;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getBet() {
        return bet;
    }

    // Abstract method for the player's turn
    public abstract void takeTurn(BlackjackGameController gameController);
}

package com.game.BlackJack;

import java.util.ArrayList;

public abstract class Player {
    private String name;
    private ArrayList<Card> hand;
    private int balance;
    private int bet;

    public Player(String name, int balance) {
        this.name = name;
        this.balance = balance;
        this.hand = new ArrayList<>();
    }

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
            if (card.getRank().equals("Ace")) aces++;
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public boolean isBusted() {
        return calculateHandValue() > 21;
    }

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

    public abstract void takeTurn(GameController gameController);

    public void setBet(int bet) {
        this.bet = bet;
    }

    public int getBet() {
        return bet;
    }
}

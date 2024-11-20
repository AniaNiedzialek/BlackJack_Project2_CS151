package com.game.BlackJack;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int balance;
    private int bet;
    private boolean isHuman;

    public Player(String name, int balance, boolean isHuman) {
        this.name = name;
        this.balance = balance;
        this.isHuman = isHuman;
        hand = new ArrayList<>();
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

    public int getBalance() {
        return balance;
    }

    public void adjustBalance(int amount) {
        balance += amount;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public boolean isHuman() {
        return isHuman;
    }

    @Override
    public String toString() {
        return name;
    }
}

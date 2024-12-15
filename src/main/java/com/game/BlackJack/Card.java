package com.game.BlackJack;

public class Card {
    private String rank;
    private String suit;
    private int value; // Numeric value for Blackjack

    public Card(String rank, String suit, int value) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }
    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    public int getValue() {
        return value;
    
    }

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }
    
} 

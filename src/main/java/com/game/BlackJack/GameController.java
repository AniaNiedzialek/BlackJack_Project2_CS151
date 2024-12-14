
package com.game.BlackJack;

import java.util.ArrayList;

public class GameController {
    private Deck deck;
    private ArrayList<Player> players;
    private Dealer dealer;
    private int currentPlayerIndex;

    public GameController() {
        // Initialize the deck and shuffle
        deck = new Deck();
        deck.shuffle();

        // Initialize players
        players = new ArrayList<>();
        players.add(new HumanPlayer("Human", 1000)); // Human player
        players.add(new AIPlayer("AI Player 1", 1000)); // AI Player 1
        players.add(new AIPlayer("AI Player 2", 1000)); // AI Player 2

        // Initialize dealer
        dealer = new Dealer();

        // Start with the first player
        currentPlayerIndex = 0;
    }

    /**
     * Starts a new round by clearing hands, dealing cards, and resetting the turn index.
     */
    public void startRound() {
        // Clear all hands
        for (Player player : players) {
            player.clearHand();
        }
        dealer.clearHand();

        // Deal two cards to each player and the dealer
        for (Player player : players) {
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
        }
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());

        // Reset the turn to the first player
        currentPlayerIndex = 0;
    }

    /**
     * Gets the current player whose turn it is.
     * 
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Advances the turn to the next player.
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Checks if the round is over (all players have taken their turns).
     * 
     * @return True if the round is over, false otherwise.
     */
    public boolean isRoundOver() {
        return currentPlayerIndex == players.size();
    }

    /**
     * Dealer plays its turn following the rules (hit on 17 or less).
     */
    public void dealerPlay() {
        dealer.takeTurn(this);
    }

    /**
     * Resolves the round by determining winners and losers and updating balances.
     * 
     * @return The results of the round.
     */
    public ArrayList<String> resolveRound() {
        ArrayList<String> results = new ArrayList<>();
        int dealerValue = dealer.calculateHandValue();
        boolean dealerBusted = dealer.isBusted();

        for (Player player : players) {
            int playerValue = player.calculateHandValue();

            if (player.isBusted()) {
                results.add(player.getName() + " busted and lost their bet.");
            } else if (dealerBusted || playerValue > dealerValue) {
                results.add(player.getName() + " wins!");
            } else if (playerValue == dealerValue) {
                results.add(player.getName() + " ties with the dealer.");
            } else {
                results.add(player.getName() + " loses.");
            }
        }

        return results;
    }

    public Deck getDeck() {
        return deck;
    }

    public Dealer getDealer() {
        return dealer;
    }

    private int getCardValue(String rank) {
        switch (rank) {
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "Jack": return 10;
            case "Queen": return 10;
            case "King": return 10;
            case "Ace": return 11; // Can be adjusted for Ace's dual value
            default: throw new IllegalArgumentException("Unknown rank: " + rank);
        }
    }

    public int calculateHandValue(Player player) {
        int value = 0;
        int aces = 0;
    
        for (Card card : player.getHand()) {
            value += getCardValue(card.getRank());
            if (card.getRank().equals("Ace")) aces++;
        }
    
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }
    
        return value;
    }

    public HumanPlayer getHumanPlayer() {
        return (HumanPlayer) players.get(0); // Assuming the first player is always the human player
    }
    
    public AIPlayer getAIPlayer1() {
        return (AIPlayer) players.get(1); // Assuming the second player is AI Player 1
    }
    
    public AIPlayer getAIPlayer2() {
        return (AIPlayer) players.get(2); // Assuming the third player is AI Player 2
    }

    public ArrayList<Player> getPlayers() {
        return players; // Assuming `players` is the ArrayList<Player> storing all players.
    }
    
    
}

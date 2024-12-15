package com.game.BlackJack;

import java.util.ArrayList;

import com.game.ScoreTracker;
import com.game.SessionManager;

public class BlackjackGameController {
    private Deck deck;
    private ArrayList<Player> players;
    private Dealer dealer;
    private int currentPlayerIndex;

    public BlackjackGameController() {
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

    public void initializeBets() {
        for (Player player : players) {
            if (player instanceof AIPlayer) {
                // AI players place a default bet
                ((AIPlayer) player).placeBet(10); // Example: AI always bets $10
            }
        }
    }
    

    /**
     * Starts a new round by clearing hands, dealing cards, and resetting the turn index.
     */
    public void startRound() {
        // Clear hands and reset bets
        for (Player player : players) {
            player.clearHand();
        }
        dealer.clearHand();

        // Initialize deck and deal cards
        deck = new Deck();
        deck.shuffle();

        for (Player player : players) {
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
        }
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());

        currentPlayerIndex = 0;
    }

    public Player getCurrentPlayer() {
        if (currentPlayerIndex >= players.size()) {
            throw new IllegalStateException("No current player: all turns are completed.");
        }
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex++;

        if (currentPlayerIndex >= players.size()) {
            System.out.println("All players have completed their turns. Dealer's turn begins.");
            return;
        }

        Player currentPlayer = players.get(currentPlayerIndex);

        // Threaded delay for AI Players
        if (currentPlayer instanceof AIPlayer) {
            new Thread(() -> {
                try {
                    Thread.sleep(1500); // 1.5-second delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Next Turn - Current Player: " + currentPlayer.getName());
            }).start();
        } else {
            System.out.println("Next Turn - Current Player: " + currentPlayer.getName());
        }
    }

    public void setCurrentPlayerIndex(int index) {
        currentPlayerIndex = index;
    }

    public boolean isRoundOver() {
        return currentPlayerIndex >= players.size();
    }

    public void dealerPlay() {
        System.out.println("Dealer's turn begins.");
        dealer.takeTurn(this);
    }

    public ArrayList<String> resolveRound() {
        ArrayList<String> results = new ArrayList<>();
        int dealerValue = dealer.calculateHandValue();
        boolean dealerBusted = dealer.isBusted();
    
        for (Player player : players) {
            int playerValue = player.calculateHandValue();
            if (player.isBusted()) {
                results.add(player.getName() + " busted and lost their bet.");
                player.loseBet();
            } else if (dealerBusted || playerValue > dealerValue) {
                results.add(player.getName() + " wins!");
                player.winBet();
                ScoreTracker.writeScoreFile("blackjack", SessionManager.getInstance().getCurrentUser(), String.valueOf(player.getBalance()));
            } else if (playerValue == dealerValue) {
                results.add(player.getName() + " ties with the dealer.");
                player.tieBet();
            } else {
                results.add(player.getName() + " loses.");
                player.loseBet();
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

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
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
            case "Ace": return 11;
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
        return (HumanPlayer) players.get(0);
    }

    public AIPlayer getAIPlayer1() {
        return (AIPlayer) players.get(1);
    }

    public AIPlayer getAIPlayer2() {
        return (AIPlayer) players.get(2);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void loadSaveStateString(String saveStateString) {
        String[] sections = saveStateString.split("\\|");
        players.clear();
    
        for (String section : sections) {
            if (section.startsWith("Dealer:")) {
                // Load dealer's cards
                dealer.clearHand();
                String[] cards = section.substring(7).split(",");
                for (String card : cards) {
                    if (!card.isEmpty()) {
                        String[] rankAndSuit = card.split("-");
                        dealer.addCard(new Card(rankAndSuit[0], rankAndSuit[1]));
                    }
                }
            } else if (section.startsWith("Turn:")) {
                // Load turn information
                currentPlayerIndex = Integer.parseInt(section.substring(5));
            } else {
                // Load player's information
                String[] parts = section.split(":");
                String name = parts[0];
                int balance = Integer.parseInt(parts[1]);
                int bet = Integer.parseInt(parts[2]);
                String[] cards = parts[3].split(",");
    
                Player player;
                if (name.equals("Human")) {
                    player = new HumanPlayer(name, balance);
                } else {
                    player = new AIPlayer(name, balance);
                }
                player.placeBet(bet);
                for (String card : cards) {
                    String[] rankAndSuit = card.split("-");
                    player.addCard(new Card(rankAndSuit[0], rankAndSuit[1]));
                }
                players.add(player);
            }
        }
    }

    public String generateSaveStateString() {
        StringBuilder saveState = new StringBuilder();
    
        // Encode players' information
        for (Player player : players) {
            saveState.append(player.getName()).append(":");
            saveState.append(player.getBalance()).append(":");
            saveState.append(player.getBet()).append(":");
            for (Card card : player.getHand()) {
                saveState.append(card.getRank()).append("-").append(card.getSuit()).append(",");
            }
            saveState.deleteCharAt(saveState.length() - 1); // Remove trailing comma
            saveState.append("|");
        }
    
        // Encode dealer's information
        saveState.append("Dealer:");
        for (Card card : dealer.getHand()) {
            saveState.append(card.getRank()).append("-").append(card.getSuit()).append(",");
        }
        if (!dealer.getHand().isEmpty()) {
            saveState.deleteCharAt(saveState.length() - 1); // Remove trailing comma
        }
        saveState.append("|");
    
        // Add turn information
        saveState.append("Turn:").append(currentPlayerIndex);
    
        return saveState.toString();
    }
    
    
}

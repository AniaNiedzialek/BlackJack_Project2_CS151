// package com.game.BlackJack;

// import java.util.ArrayList;

// public class GameController {
//     private Deck deck;
//     private ArrayList<Player> players;
//     private Player dealer;
//     private int currentPlayerIndex;

//     public GameController() {
//         // Initialize the deck and shuffle
//         deck = new Deck();
//         deck.shuffle();

//         // Initialize players
//         players = new ArrayList<>();
//         players.add(new Player("Human", 1000, true)); // Human player
//         players.add(new Player("AI Player 1", 1000, false)); // AI Player 1
//         players.add(new Player("AI Player 2", 1000, false)); // AI Player 2

//         // Initialize dealer
//         dealer = new Player("Dealer", 0, false);

//         // Start with the first player
//         currentPlayerIndex = 0;
//     }

//     /**
//      * Starts a new round by clearing hands, dealing cards, and resetting the turn index.
//      */
//     public void startRound() {
//         // Clear all hands
//         for (Player player : players) {
//             player.clearHand();
//             player.setBet(0); // Reset bets
//         }
//         dealer.clearHand();

//         // Deal two cards to each player and the dealer
//         for (Player player : players) {
//             player.addCard(deck.dealCard());
//             player.addCard(deck.dealCard());
//         }
//         dealer.addCard(deck.dealCard());
//         dealer.addCard(deck.dealCard());

//         // Reset the turn to the first player
//         currentPlayerIndex = 0;
//     }

//     /**
//      * Gets the current player whose turn it is.
//      * 
//      * @return The current player.
//      */
//     public Player getCurrentPlayer() {
//         return players.get(currentPlayerIndex);
//     }

//     /**
//      * Advances the turn to the next player.
//      */
//     public void nextTurn() {
//         currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
//     }

//     /**
//      * Checks if the round is over (all players have taken their turns).
//      * 
//      * @return True if the round is over, false otherwise.
//      */
//     public boolean isRoundOver() {
//         return currentPlayerIndex == players.size();
//     }

//     /**
//      * Dealer plays its turn following the rules (hit on 17 or less).
//      */
//     public void dealerPlay() {
//         while (dealer.calculateHandValue() < 17) {
//             dealer.addCard(deck.dealCard());
//         }
//     }

//     // Resolves the round by determining winners and losers and updating balances.
     
//     public ArrayList<String> resolveRound() {
//         ArrayList<String> results = new ArrayList<>();
//         int dealerValue = dealer.calculateHandValue();
//         boolean dealerBusted = dealer.isBusted();
    
//         for (Player player : players) {
//             int playerValue = player.calculateHandValue();
    
//             if (player.isBusted()) {
//                 results.add(player.toString() + " busted and lost " + player.getBet());
//                 player.adjustBalance(-player.getBet());
//             } else if (dealerBusted || playerValue > dealerValue) {
//                 results.add(player.toString() + " wins " + player.getBet());
//                 player.adjustBalance(player.getBet());
//             } else if (playerValue == dealerValue) {
//                 results.add(player.toString() + " ties with the dealer. Bet is returned.");
//             } else {
//                 results.add(player.toString() + " loses " + player.getBet());
//                 player.adjustBalance(-player.getBet());
//             }
//         }
    
//         return results;
//     }
    

//     /**
//      * Saves the current game state as a string.
//      * 
//      * @return The saved game state.
//      */
//     public String saveGameState() {
//         StringBuilder saveState = new StringBuilder();

//         // Save player information
//         for (Player player : players) {
//             saveState.append(player.toString()).append(":");
//             saveState.append(player.getBalance()).append(":");
//             saveState.append(player.getBet()).append(":");

//             // Save player's cards
//             for (Card card : player.getHand()) {
//                 saveState.append(card.getRank()).append("-").append(card.getSuit()).append(",");
//             }
//             saveState.deleteCharAt(saveState.length() - 1); // Remove trailing comma
//             saveState.append("|");
//         }

//         // Save dealer information
//         saveState.append("Dealer:");
//         for (Card card : dealer.getHand()) {
//             saveState.append(card.getRank()).append("-").append(card.getSuit()).append(",");
//         }
//         saveState.deleteCharAt(saveState.length() - 1); // Remove trailing comma
//         saveState.append("|");

//         // Save whose turn it is
//         saveState.append("Turn:").append(currentPlayerIndex);

//         return saveState.toString();
//     }

//     /**
//      * Loads a game state from a string.
//      * 
//      * @param saveState The saved game state.
//      */
//     public void loadGameState(String saveState) {
//         String[] sections = saveState.split("\\|");
//         int playerIndex = 0;

//         for (String section : sections) {
//             if (section.startsWith("Dealer")) {
//                 // Load dealer's hand
//                 String[] dealerCards = section.split(":")[1].split(",");
//                 dealer.clearHand();
//                 for (String cardData : dealerCards) {
//                     String[] cardParts = cardData.split("-");
//                     dealer.addCard(new Card(cardParts[0], cardParts[1], getCardValue(cardParts[0])));
//                 }
//             } else if (section.startsWith("Turn")) {
//                 // Load current player's turn
//                 currentPlayerIndex = Integer.parseInt(section.split(":")[1]);
//             } else {
//                 // Load player's data
//                 String[] playerData = section.split(":");
//                 Player player = players.get(playerIndex++);
//                 player.clearHand();
//                 player.adjustBalance(Integer.parseInt(playerData[1]));
//                 player.setBet(Integer.parseInt(playerData[2]));

//                 // Load player's cards
//                 String[] cardData = playerData[3].split(",");
//                 for (String card : cardData) {
//                     String[] cardParts = card.split("-");
//                     player.addCard(new Card(cardParts[0], cardParts[1], getCardValue(cardParts[0])));
//                 }
//             }
//         }
//     }

//     /**
//      * Gets the list of players.
//      * 
//      * @return The list of players.
//      */
//     public ArrayList<Player> getPlayers() {
//         return players;
//     }

//     /**
//      * Gets the dealer.
//      * 
//      * @return The dealer.
//      */
//     public Player getDealer() {
//         return dealer;
//     }

//     /**
//      * Gets the numeric value of a card's rank.
//      * 
//      * @param rank The rank of the card.
//      * @return The numeric value of the card.
//      */
//     private int getCardValue(String rank) {
//         switch (rank) {
//             case "2": return 2;
//             case "3": return 3;
//             case "4": return 4;
//             case "5": return 5;
//             case "6": return 6;
//             case "7": return 7;
//             case "8": return 8;
//             case "9": return 9;
//             case "10": return 10;
//             case "Jack": return 10;
//             case "Queen": return 10;
//             case "King": return 10;
//             case "Ace": return 11;
//             default: throw new IllegalArgumentException("Unknown rank: " + rank);
//         }
//     }
//     public Deck getDeck() {
//         return deck;
//     }
    
// }
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

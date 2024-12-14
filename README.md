# Game Project2 CS 151

## Overview
**Welcome to the Sunny Games!**
The following project implements a Game Manager and two games: BlackJack and Snake, using JavaFX. 
Both games are implemented from scratch in OOP style with the goal of being fully playable. The manager also support persisten storage for creating and logging in users to keep track of high scores across sessions.


## **BlackJack**
The game supports 1 human player and 3 automated characters (2 other normal players and dealer). 

## **Features**:
- **Playable Blackjack Game**
  - One human player and two AI players.
  - Dealer follows standard Blackjack rules.
  - Simple controls: Hit, Stand, and Start a New Game.
    
## **How to Play**

1. Launch the application.
2. The welcome screen offers two options:
   - **New Game:** Starts a new game of Blackjack.
   - **Exit:** Closes the application.
3. Place a bet and begin playing:
   - *Hit:* Draw another card.
   - *Stand:* End your turn.
   - The AI players and the dealer will take their turns automatically.
4. The game will determine the winner based on standard Blackjack rules:
   - Closest to 21 without exceeding it wins.
   - A tie results in the bet being returned.

## **Rules of Blackjack**

1. Each player is dealt two cards initially.
2. Players aim to get a total card value of 21 or as close as possible without exceeding it.
3. Card values:
   - Number cards are worth their face value.
   - Face cards (Jack, Queen, King) are worth 10.
   - Aces are worth 1 or 11, depending on the total value of the hand.
4. The dealer must "Hit" until their hand totals at least 17.

## **Design**
- **AIPlayer**: Represents an AI-controlled player with automated decision-making logic for actions such as hitting or standing based on the current hand value.
- **BlackJackUI**: Provides the graphical user interface for the game, including the welcome screen, game table, and user interactions like buttons for "Hit" and "Stand."
- **Card**: Models a playing card with attributes such as rank, suit, and value, and includes functionality for representing card details.
- **Dealer**: Implements the dealer's behavior, including hitting until reaching a hand value of at least 17, as per Blackjack rules.
- **Deck**:  Manages the deck of cards, including shuffling, dealing cards, and keeping track of remaining cards.
- **GameController**: Handles the core game logic, including managing turns, player actions, betting, and determining the winner at the end of each round.
- **HumanPlayer**: Represents the human player, allowing manual actions such as hitting and standing, and managing their bet and balance.
- **Player**: Serves as the abstract base class for all players (human, AI, dealer), providing common attributes and behaviors like hand management and balance tracking.
 
---

## Snake





## Contributions
- **Sunny Doan**: Implemented the Game Manager, worked on High Scores
- **Mohammed Nassar**: Implemented Snake Game, adding key features to make the game fully playable
- **Ania Niedzialek**: Implemented BlackJack Game, adding key features for the game layout, worked on README


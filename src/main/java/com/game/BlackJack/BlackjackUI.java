// package com.game.BlackJack;

// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.TextArea;
// import javafx.scene.control.TextInputDialog;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.*;
// import javafx.stage.Stage;

// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.util.ArrayList;

// public class BlackjackUI extends Application {

//     private GameController gameController;

//     private HBox playerBox; // To display the human player's cards
//     private HBox dealerBox; // To display the dealer's cards
//     private HBox aiPlayer1Box; // To display AI Player 1's cards
//     private HBox aiPlayer2Box; // To display AI Player 2's cards

//     @Override
//     public void start(Stage primaryStage) {
//         // Initialize Game Controller
//         gameController = new GameController();
    
//         // Table layout
//         BorderPane tableLayout = new BorderPane();
//         tableLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #121212, #0A0A0A);");
    
//         // Dealer's cards (top of the screen)
//         dealerBox = new HBox(10);
//         dealerBox.setAlignment(Pos.CENTER);
//         Label dealerLabel = new Label("Dealer");
//         VBox dealerArea = new VBox(15, dealerLabel, dealerBox);
//         dealerArea.setAlignment(Pos.CENTER);
//         dealerArea.setPadding(new Insets(20, 0, 20, 0)); // Add padding above and below
//         dealerLabel.setStyle("-fx-text-fill: yellow");
//         tableLayout.setTop(dealerArea);
    
//         // AI Player 1's cards (left of the screen)
//         aiPlayer1Box = new HBox(10);
//         aiPlayer1Box.setAlignment(Pos.CENTER);
//         Label aiPlayer1Label = new Label("AI Player 1");
//         VBox aiPlayer1Area = new VBox(15, aiPlayer1Label, aiPlayer1Box);
//         aiPlayer1Area.setAlignment(Pos.CENTER);
//         aiPlayer1Area.setPadding(new Insets(0, 20, 0, 20)); // Add padding left and right
//         aiPlayer1Label.setStyle("-fx-text-fill: yellow");
//         tableLayout.setLeft(aiPlayer1Area);
    
//         // AI Player 2's cards (right of the screen)
//         aiPlayer2Box = new HBox(10);
//         aiPlayer2Box.setAlignment(Pos.CENTER);
//         Label aiPlayer2Label = new Label("AI Player 2");
//         VBox aiPlayer2Area = new VBox(15, aiPlayer2Label, aiPlayer2Box);
//         aiPlayer2Area.setAlignment(Pos.CENTER);
//         aiPlayer2Area.setPadding(new Insets(0, 20, 0, 20)); // Add padding left and right
//         aiPlayer2Label.setStyle("-fx-text-fill: yellow");
//         tableLayout.setRight(aiPlayer2Area);
    
//         // Human Player's cards (bottom of the screen)
//         playerBox = new HBox(10);
//         playerBox.setAlignment(Pos.CENTER);
//         Label playerLabel = new Label("You");
//         VBox playerCardsArea = new VBox(15, playerLabel, playerBox);
//         playerCardsArea.setAlignment(Pos.CENTER);
//         playerCardsArea.setPadding(new Insets(10, 0, 10, 0)); // Add padding above and below
//         playerLabel.setStyle("-fx-text-fill: yellow");
    
//         // Control buttons below the human player's cards
//         Button newGameButton = new Button("New Game");
//         Button hitButton = new Button("Hit");
//         Button standButton = new Button("Stand");
//         Button saveButton = new Button("Save Game");
//         Button loadButton = new Button("Load Game");
    
//         // Disable buttons until the game starts
//         hitButton.setDisable(true);
//         standButton.setDisable(true);
    
//         newGameButton.setOnAction(e -> startNewGame(hitButton, standButton));
//         hitButton.setOnAction(e -> hit());
//         standButton.setOnAction(e -> stand());
//         saveButton.setOnAction(e -> saveGame());
//         loadButton.setOnAction(e -> loadGame(primaryStage));
    
//         HBox buttonBox = new HBox(10, newGameButton, hitButton, standButton, saveButton, loadButton);
//         buttonBox.setAlignment(Pos.CENTER);
//         buttonBox.setPadding(new Insets(10, 0, 10, 0)); // Add padding around buttons
    
//         // Combine human player's cards and buttons in one VBox
//         VBox bottomArea = new VBox(10, playerCardsArea, buttonBox);
//         bottomArea.setAlignment(Pos.CENTER);
//         tableLayout.setBottom(bottomArea);
    
//         // Create and set the scene
//         Scene scene = new Scene(tableLayout, 900, 700); // Adjusted scene size
//         primaryStage.setTitle("BlackJack Game!");
//         primaryStage.setScene(scene);
//         primaryStage.show();
//     }
    


//     private void startNewGame(Button hitButton, Button standButton) {
//         for (Player player : gameController.getPlayers()) {
//             if (player.isHuman()) {
//                 // Prompt human player to place a bet
//                 TextInputDialog betDialog = new TextInputDialog("100");
//                 betDialog.setTitle("Place Your Bet");
//                 betDialog.setHeaderText("Your Balance: " + player.getBalance());
//                 betDialog.setContentText("Enter your bet amount:");
    
//                 betDialog.showAndWait().ifPresent(bet -> {
//                     int betAmount = Integer.parseInt(bet);
//                     if (betAmount > 0 && betAmount <= player.getBalance()) {
//                         player.setBet(betAmount);
//                         System.out.println(player.toString() + " bet " + betAmount);
//                     } else {
//                         System.out.println("Invalid bet. Setting bet to 100.");
//                         player.setBet(100);
//                     }
//                 });
//             } else {
//                 // AI players bet a fixed amount
//                 player.setBet(100);
//                 System.out.println(player.toString() + " bet 100");
//             }
//         }
    
//         gameController.startRound();
//         System.out.println("New round started!");
//         hitButton.setDisable(false);
//         standButton.setDisable(false);
//         updateTable();
//     }
    

//     private void hit() {
//         Player currentPlayer = gameController.getCurrentPlayer();
//         currentPlayer.addCard(gameController.getDeck().dealCard());
//         System.out.println(currentPlayer.toString() + " hits.");
    
//         if (currentPlayer.isBusted()) {
//             System.out.println(currentPlayer.toString() + " busted!");
//             gameController.nextTurn();
//         }
    
//         updateTable();
//     }
    
    


//     private void stand() {
//         System.out.println(gameController.getCurrentPlayer().toString() + " stands.");
//         gameController.nextTurn();
//         if (gameController.isRoundOver()) {
//             gameController.dealerPlay();
//             endRound();
//         }
//         updateTable();
//     }

//     private void endRound() {
//         ArrayList<String> results = gameController.resolveRound();
//         for (String result : results) {
//             System.out.println(result);
//         }
    
//         // Disable buttons and show balances
//         System.out.println("Round ended.");
//         System.out.println("Player Balances:");
//         for (Player player : gameController.getPlayers()) {
//             System.out.println(player.toString() + ": " + player.getBalance());
//         }
//     }
    

//     private void saveGame() {
//         String saveState = gameController.saveGameState();
//         System.out.println("Game saved! Save State:\n" + saveState);
//     }
    

//     private void loadGame(Stage primaryStage) {
//         TextInputDialog dialog = new TextInputDialog();
//         dialog.setTitle("Load Game");
//         dialog.setHeaderText("Enter Save State String:");
//         dialog.setContentText("Save State:");
    
//         dialog.showAndWait().ifPresent(saveState -> {
//             gameController.loadGameState(saveState);
//             System.out.println("Game loaded from save state!");
//             updateTable(); // Update the card display
//         });
//     }
    
    

//     private void updateTable() {
//         // Clear all boxes
//         playerBox.getChildren().clear();
//         dealerBox.getChildren().clear();
//         aiPlayer1Box.getChildren().clear();
//         aiPlayer2Box.getChildren().clear();

//         // Update cards for each player
//         updateCardDisplay(playerBox, gameController.getCurrentPlayer().getHand());
//         updateCardDisplay(dealerBox, gameController.getDealer().getHand());
//         updateCardDisplay(aiPlayer1Box, gameController.getPlayers().get(1).getHand());
//         updateCardDisplay(aiPlayer2Box, gameController.getPlayers().get(2).getHand());
//     }

//     private void updateCardDisplay(HBox cardBox, ArrayList<Card> hand) {
//         for (Card card : hand) {
//             try {
//                 String imagePath = "resources/cards/" + card.getRank() + "_of_" + card.getSuit() + ".png";
//                 ImageView cardImage = new ImageView(new Image(new FileInputStream(imagePath)));
//                 cardImage.setFitWidth(100);
//                 cardImage.setPreserveRatio(true);
//                 cardBox.getChildren().add(cardImage);
//             } catch (FileNotFoundException e) {
//                 System.out.println("Error loading card image: " + e.getMessage() + "\n");
//             }
//         }
//     }

//     public static void main(String[] args) {
//         launch(args);
//     }
// }
package com.game.BlackJack;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BlackjackUI extends Application {

    private GameController gameController;

    private HBox playerBox;    // Human player's cards
    private HBox dealerBox;    // Dealer's cards
    private HBox aiPlayer1Box; // AI Player 1's cards
    private HBox aiPlayer2Box; // AI Player 2's cards

    @Override
    public void start(Stage primaryStage) {
        // Initialize the game controller
        gameController = new GameController();

        // Get screen size
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Table layout
        BorderPane tableLayout = new BorderPane();
        tableLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #121212, #0A0A0A);");

        // Dealer's cards (top)
        dealerBox = createPlayerBox();
        tableLayout.setTop(createVBox("Dealer", dealerBox, screenHeight * 0.1));

        // AI Player 1's cards (left)
        aiPlayer1Box = createPlayerBox();
        tableLayout.setLeft(createVBox("AI Player 1", aiPlayer1Box, screenHeight * 0.35));

        // AI Player 2's cards (right)
        aiPlayer2Box = createPlayerBox();
        tableLayout.setRight(createVBox("AI Player 2", aiPlayer2Box, screenHeight * 0.35));

        // Human Player's cards (bottom)
        playerBox = createPlayerBox();
        tableLayout.setBottom(createBottomArea(screenWidth, screenHeight * 0.15));

        // Set up the scene
        Scene scene = new Scene(tableLayout, screenWidth, screenHeight);
        primaryStage.setTitle("Blackjack Game");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Full-screen mode
        primaryStage.show();
    }

    private VBox createBottomArea(double width, double height) {
        Button newGameButton = new Button("New Game");
        Button hitButton = new Button("Hit");
        Button standButton = new Button("Stand");
        Button saveButton = new Button("Save Game");
        Button loadButton = new Button("Load Game");

        hitButton.setDisable(true);
        standButton.setDisable(true);

        newGameButton.setOnAction(e -> startNewGame(hitButton, standButton));
        hitButton.setOnAction(e -> hit());
        standButton.setOnAction(e -> stand());
        saveButton.setOnAction(e -> saveGame());
        loadButton.setOnAction(e -> loadGame());

        HBox buttonBox = new HBox(10, newGameButton, hitButton, standButton, saveButton, loadButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setPrefHeight(height * 0.5);

        VBox bottomArea = new VBox(10, createVBox("You", playerBox, height * 0.5), buttonBox);
        bottomArea.setAlignment(Pos.CENTER);
        bottomArea.setPadding(new Insets(10));
        bottomArea.setPrefHeight(height);
        return bottomArea;
    }

    private VBox createVBox(String label, HBox box, double height) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: yellow; -fx-font-size: 20px;");
        VBox vbox = new VBox(10, lbl, box);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setPrefHeight(height);
        return vbox;
    }

    private HBox createPlayerBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void startNewGame(Button hitButton, Button standButton) {
        for (Player player : gameController.getPlayers()) {
            if (player.isHuman()) {
                // Prompt human player to place a bet
                TextInputDialog betDialog = new TextInputDialog("100");
                betDialog.setTitle("Place Your Bet");
                betDialog.setHeaderText("Your Balance: " + player.getBalance());
                betDialog.setContentText("Enter your bet amount:");

                betDialog.showAndWait().ifPresent(bet -> {
                    int betAmount = Integer.parseInt(bet);
                    if (betAmount > 0 && betAmount <= player.getBalance()) {
                        player.setBet(betAmount);
                        System.out.println(player + " bet " + betAmount);
                    } else {
                        System.out.println("Invalid bet. Setting bet to 100.");
                        player.setBet(100);
                    }
                });
            } else {
                // AI players bet a fixed amount
                player.setBet(100);
                System.out.println(player + " bet 100");
            }
        }

        gameController.startRound();
        hitButton.setDisable(false);
        standButton.setDisable(false);
        updateTable();
    }

    private void hit() {
        Player currentPlayer = gameController.getCurrentPlayer();
        currentPlayer.addCard(gameController.getDeck().dealCard());
        System.out.println(currentPlayer + " hits.");

        if (currentPlayer.isBusted()) {
            System.out.println(currentPlayer + " busted!");
            gameController.nextTurn();
        }

        updateTable();
    }

    private void stand() {
        System.out.println(gameController.getCurrentPlayer() + " stands.");
        gameController.nextTurn();
        if (gameController.isRoundOver()) {
            gameController.dealerPlay();
            endRound();
        }
        updateTable();
    }

    private void endRound() {
        ArrayList<String> results = gameController.resolveRound();
        results.forEach(System.out::println);
    }

    private void saveGame() {
        String saveState = gameController.saveGameState();
        System.out.println("Game saved! Save State:\n" + saveState);
    }

    private void loadGame() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load Game");
        dialog.setHeaderText("Enter Save State String:");
        dialog.setContentText("Save State:");

        dialog.showAndWait().ifPresent(saveState -> {
            gameController.loadGameState(saveState);
            System.out.println("Game loaded from save state!");
            updateTable();
        });
    }

    private void updateTable() {
        playerBox.getChildren().clear();
        dealerBox.getChildren().clear();
        aiPlayer1Box.getChildren().clear();
        aiPlayer2Box.getChildren().clear();

        updateCardDisplay(playerBox, gameController.getPlayers().get(0).getHand());
        updateCardDisplay(aiPlayer1Box, gameController.getPlayers().get(1).getHand());
        updateCardDisplay(aiPlayer2Box, gameController.getPlayers().get(2).getHand());
        updateCardDisplay(dealerBox, gameController.getDealer().getHand());
    }

    private void updateCardDisplay(HBox cardBox, ArrayList<Card> hand) {
        for (Card card : hand) {
            try {
                String imagePath = "resources/cards/" + card.getRank() + "_of_" + card.getSuit() + ".png";
                ImageView cardImage = new ImageView(new Image(new FileInputStream(imagePath)));
                cardImage.setFitWidth(100);
                cardImage.setPreserveRatio(true);
                cardBox.getChildren().add(cardImage);
            } catch (FileNotFoundException e) {
                System.out.println("Error loading card image: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

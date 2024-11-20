package com.game.BlackJack;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BlackjackUI extends Application {

    private GameController gameController;

    private HBox playerBox; // To display the human player's cards
    private HBox dealerBox; // To display the dealer's cards
    private HBox aiPlayer1Box; // To display AI Player 1's cards
    private HBox aiPlayer2Box; // To display AI Player 2's cards

    @Override
    public void start(Stage primaryStage) {
        // Initialize Game Controller
        gameController = new GameController();

        // Table layout
        BorderPane tableLayout = new BorderPane();
        tableLayout.setStyle("-fx-background-color: green;");

        // Dealer's cards (top of the screen)
        dealerBox = new HBox(10);
        dealerBox.setAlignment(Pos.CENTER);
        Label dealerLabel = new Label("Dealer");
        VBox dealerArea = new VBox(5, dealerLabel, dealerBox);
        dealerArea.setAlignment(Pos.CENTER);
        tableLayout.setTop(dealerArea);

        // AI Player 1's cards (left of the screen)
        aiPlayer1Box = new HBox(10);
        aiPlayer1Box.setAlignment(Pos.CENTER);
        Label aiPlayer1Label = new Label("AI Player 1");
        VBox aiPlayer1Area = new VBox(5, aiPlayer1Label, aiPlayer1Box);
        aiPlayer1Area.setAlignment(Pos.CENTER);
        tableLayout.setLeft(aiPlayer1Area);

        // AI Player 2's cards (right of the screen)
        aiPlayer2Box = new HBox(10);
        aiPlayer2Box.setAlignment(Pos.CENTER);
        Label aiPlayer2Label = new Label("AI Player 2");
        VBox aiPlayer2Area = new VBox(5, aiPlayer2Label, aiPlayer2Box);
        aiPlayer2Area.setAlignment(Pos.CENTER);
        tableLayout.setRight(aiPlayer2Area);

        // Human Player's cards (bottom of the screen)
        playerBox = new HBox(10);
        playerBox.setAlignment(Pos.CENTER);
        Label playerLabel = new Label("You");
        VBox playerArea = new VBox(5, playerLabel, playerBox);
        playerArea.setAlignment(Pos.CENTER);
        tableLayout.setBottom(playerArea);

        // Buttons (center)
        Button newGameButton = new Button("New Game");
        Button hitButton = new Button("Hit");
        Button standButton = new Button("Stand");
        Button saveButton = new Button("Save Game");
        Button loadButton = new Button("Load Game");

        // Disable buttons until the game starts
        hitButton.setDisable(true);
        standButton.setDisable(true);

        newGameButton.setOnAction(e -> startNewGame(hitButton, standButton));
        hitButton.setOnAction(e -> hit());
        standButton.setOnAction(e -> stand());
        saveButton.setOnAction(e -> saveGame());
        loadButton.setOnAction(e -> loadGame(primaryStage));

        HBox buttonBox = new HBox(10, newGameButton, hitButton, standButton, saveButton, loadButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(10, buttonBox);
        centerBox.setAlignment(Pos.CENTER);
        tableLayout.setCenter(centerBox);

        // Create and set the scene
        Scene scene = new Scene(tableLayout, 800, 600);
        primaryStage.setTitle("Blackjack");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startNewGame(Button hitButton, Button standButton) {
        gameController.startRound();
        System.out.println("New round started!");
        hitButton.setDisable(false);
        standButton.setDisable(false);
        updateTable();
    }

    private void hit() {
        Player currentPlayer = gameController.getCurrentPlayer();
        currentPlayer.addCard(gameController.getDeck().dealCard());
        System.out.println(currentPlayer.toString() + " hits.");

        if (currentPlayer.isBusted()) {
            System.out.println(currentPlayer.toString() + " busted!");
            gameController.nextTurn();
        }

        updateTable();
    }

    private void stand() {
        System.out.println(gameController.getCurrentPlayer().toString() + " stands.");
        gameController.nextTurn();
        updateTable();
    }

    private void saveGame() {
        String saveState = gameController.saveGameState();
        System.out.println("Game saved! Save State:\n" + saveState);
    }

    private void loadGame(Stage primaryStage) {
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

        updateCardDisplay(playerBox, gameController.getCurrentPlayer().getHand());
        updateCardDisplay(dealerBox, gameController.getDealer().getHand());
        updateCardDisplay(aiPlayer1Box, gameController.getPlayers().get(1).getHand());
        updateCardDisplay(aiPlayer2Box, gameController.getPlayers().get(2).getHand());
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

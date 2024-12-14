package com.game.BlackJack;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BlackjackUI extends Application {

    private GameController gameController;

    private HBox playerBox;
    private HBox dealerBox;
    private HBox aiPlayer1Box;
    private HBox aiPlayer2Box;
    private Scene welcomeScene;
    private Scene gameScene;
    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;

        // Create the welcome scene
        createWelcomeScene();

        // Create the game scene
        createGameScene();

        // Set the stage to the welcome scene
        mainStage.setScene(welcomeScene);
        mainStage.setTitle("Welcome to Blackjack!");
        mainStage.show();
    }

    private void createWelcomeScene() {
        // Layout for the welcome screen
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(20));
         // Load the background image
    try {
        String backgroundPath = "resources/background/welcome_background.png"; 
        Image backgroundImage = new Image(new FileInputStream(backgroundPath));
        BackgroundImage bgImage = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(
                BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true
            )
        );
        welcomeLayout.setBackground(new Background(bgImage));
    } catch (FileNotFoundException e) {
        System.out.println("Error loading background image: " + e.getMessage());
    }

        // Welcome label
        Label welcomeLabel = new Label("Welcome to Blackjack!");
        welcomeLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 43px; -fx-font-weight: bold;");

        // Buttons for "New Game" and "Exit"
        Button newGameButton = new Button("New Game");
        Button exitButton = new Button("Exit");

        newGameButton.setOnAction(e -> mainStage.setScene(gameScene)); // Transition to the game scene
        exitButton.setOnAction(e -> mainStage.close()); // Close the application

        // Add components to the layout
        welcomeLayout.getChildren().addAll(welcomeLabel, newGameButton, exitButton);

        // Create the scene
        welcomeScene = new Scene(welcomeLayout, 900, 700);
    }

    private void createGameScene() {
        gameController = new GameController();

        // Table layout
        BorderPane tableLayout = new BorderPane();
        tableLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #121212, #0A0A0A);");

        // Dealer's area
        dealerBox = createPlayerArea("Dealer", Pos.CENTER, tableLayout, "top");

        // AI Player 1's area
        aiPlayer1Box = createPlayerArea("AI Player 1", Pos.CENTER, tableLayout, "left");

        // AI Player 2's area
        aiPlayer2Box = createPlayerArea("AI Player 2", Pos.CENTER, tableLayout, "right");

        // Human Player's area
        playerBox = createPlayerArea("You", Pos.CENTER, tableLayout, "bottom");

        // Buttons for the human player
        Button newGameButton = new Button("Start!");
        Button hitButton = new Button("Hit");
        Button standButton = new Button("Stand");
        Button returnToMenuButton = new Button("Return to Menu");

        hitButton.setDisable(true);
        standButton.setDisable(true);

        newGameButton.setOnAction(e -> startNewGame(hitButton, standButton));
        hitButton.setOnAction(e -> hit());
        standButton.setOnAction(e -> stand());
        returnToMenuButton.setOnAction(e -> mainStage.setScene(welcomeScene)); // Return to the welcome scene

        HBox buttonBox = new HBox(10, newGameButton, hitButton, standButton, returnToMenuButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        VBox playerArea = new VBox(10, playerBox, buttonBox);
        playerArea.setAlignment(Pos.CENTER);
        tableLayout.setBottom(playerArea);

        // Create the game scene
        gameScene = new Scene(tableLayout, 900, 700);
    }

    private HBox createPlayerArea(String name, Pos alignment, BorderPane tableLayout, String position) {
        HBox playerBox = new HBox(10);
        playerBox.setAlignment(alignment);
    
        // Add a label for the player
        Label playerLabel = new Label(name);
        playerLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 16px; -fx-font-weight: bold;");
        VBox playerArea = new VBox(10, playerLabel, playerBox);
        playerArea.setAlignment(Pos.CENTER);
    
        // Add padding to the label and player area
        switch (position) {
            case "top": // Dealer
                playerArea.setPadding(new Insets(20, 0, 10, 0)); // Top padding
                tableLayout.setTop(playerArea);
                break;
            case "left": // AI Player 1
                playerArea.setPadding(new Insets(0, 10, 0, 20)); // Left padding
                tableLayout.setLeft(playerArea);
                break;
            case "right": // AI Player 2
                playerArea.setPadding(new Insets(0, 20, 0, 10)); // Right padding
                tableLayout.setRight(playerArea);
                break;
            case "bottom": // Human Player
                playerArea.setPadding(new Insets(10, 0, 20, 0)); // Bottom padding
                tableLayout.setBottom(playerArea);
                break;
        }
    
        return playerBox;
    }
    

    private void startNewGame(Button hitButton, Button standButton) {
        for (Player player : gameController.getPlayers()) {
            if (player instanceof HumanPlayer) {
                // Human places a bet
                TextInputDialog betDialog = new TextInputDialog("100");
                betDialog.setTitle("Place Your Bet");
                betDialog.setHeaderText("Your Balance: " + player.getBalance());
                betDialog.setContentText("Enter your bet amount:");

                betDialog.showAndWait().ifPresent(bet -> {
                    int betAmount = Integer.parseInt(bet);
                    player.setBet(Math.min(betAmount, player.getBalance()));
                });
            } else {
                // AI players bet a fixed amount
                player.setBet(100);
            }
        }

        gameController.startRound();
        hitButton.setDisable(false);
        standButton.setDisable(false);
        updateTable();
    }

    private void hit() {
        Player currentPlayer = gameController.getCurrentPlayer();
        if (currentPlayer instanceof HumanPlayer) {
            currentPlayer.addCard(gameController.getDeck().dealCard());
            if (currentPlayer.isBusted()) {
                gameController.nextTurn();
            }
            updateTable();
        }
    }

    private void stand() {
        gameController.nextTurn();
        processAITurns();
        updateTable();
    }

    private void processAITurns() {
        while (!(gameController.getCurrentPlayer() instanceof HumanPlayer)) {
            Player currentPlayer = gameController.getCurrentPlayer();
            currentPlayer.takeTurn(gameController);
            gameController.nextTurn();
        }
    }

    private void updateTable() {
        // Clear boxes
        playerBox.getChildren().clear();
        dealerBox.getChildren().clear();
        aiPlayer1Box.getChildren().clear();
        aiPlayer2Box.getChildren().clear();

        // Update cards for all players
        updateCardBox(playerBox, gameController.getHumanPlayer().getHand());
        updateCardBox(dealerBox, gameController.getDealer().getHand());
        updateCardBox(aiPlayer1Box, gameController.getAIPlayer1().getHand());
        updateCardBox(aiPlayer2Box, gameController.getAIPlayer2().getHand());
    }

    private void updateCardBox(HBox box, ArrayList<Card> hand) {
        for (Card card : hand) {
            try {
                String imagePath = "resources/cards/" + card.getRank() + "_of_" + card.getSuit() + ".png";
                ImageView cardImage = new ImageView(new Image(new FileInputStream(imagePath)));
                cardImage.setFitWidth(75);
                cardImage.setPreserveRatio(true);
                box.getChildren().add(cardImage);
            } catch (FileNotFoundException e) {
                System.out.println("Error loading card image: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
